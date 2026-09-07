'use strict';

const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const root = path.resolve(__dirname, '..');

function element() {
  let html = '';
  return {
    value: '', textContent: '', disabled: false, dataset: {}, style: {}, children: [], listeners: {},
    classList: { add() {}, remove() {}, toggle() {}, contains() { return false; } },
    get innerHTML() { return html; }, set innerHTML(value) { html = value; this.children = []; },
    addEventListener(type, callback) { this.listeners[type] = callback; },
    appendChild(child) { this.children.push(child); },
    querySelectorAll() { return []; }, querySelector() { return element(); },
    focus() {}, reset() {}, setAttribute() {}, scrollIntoView() {}, dispatchEvent() {},
  };
}

function page(filename, fetcher = async () => new Response(JSON.stringify({ success: true, data: [] }))) {
  const html = fs.readFileSync(path.join(root, filename), 'utf8');
  const ids = Array.from(html.matchAll(/\bid="([^"]+)"/g), m => m[1]);
  assert.equal(new Set(ids).size, ids.length, 'HTML IDs must be unique');
  const elements = Object.fromEntries(ids.map(id => [id, element()]));
  for (const id of ['statusFilter', 'roleFilter', 'userStatusFilter']) if (elements[id]) elements[id].value = 'all';
  const storage = new Map([['trekmate_admin_token', 'test-token']]);
  const context = vm.createContext({
    console, Headers, Response, fetch: fetcher, URL, Event,
    setTimeout() {}, requestAnimationFrame(callback) { callback(); },
    location: { origin: 'http://127.0.0.1:5000', pathname: `/admin/${filename}`, hash: '', replace(url) { this.redirect = url; } },
    sessionStorage: { getItem(key) { return storage.get(key) ?? null; }, setItem(key, value) { storage.set(key, String(value)); }, removeItem(key) { storage.delete(key); } },
    addEventListener() {}, scrollTo() {},
    document: {
      body: element(),
      getElementById(id) { assert.ok(elements[id], `Missing #${id} in ${filename}`); return elements[id]; },
      createElement: element, querySelectorAll() { return []; }, querySelector() { return element(); }, addEventListener() {},
    },
  });
  context.window = context;
  return { html, context, elements, storage, run(code) { return vm.runInContext(code, context); } };
}

test('all admin HTML scripts and local styles exist; every script parses', () => {
  for (const filename of ['index.html', 'users.html', 'login.html']) {
    const { html } = page(filename);
    for (const match of html.matchAll(/(?:src|href)="((?:js|css)\/[^"#]+)"/g)) {
      assert.ok(fs.existsSync(path.join(root, match[1])), `${filename}: ${match[1]}`);
    }
    for (const match of html.matchAll(/<script(?:\s+src="([^"]+)")?[^>]*>([\s\S]*?)<\/script>/g)) {
      new vm.Script(match[1] ? fs.readFileSync(path.join(root, match[1]), 'utf8') : match[2]);
    }
  }
});

test('API uses the running backend origin and includes the authenticated token', async () => {
  let actual;
  const p = page('login.html', async (url, options) => {
    actual = { url, options };
    return new Response(JSON.stringify({ success: true, data: {} }));
  });
  p.run(fs.readFileSync(path.join(root, 'js/api.js'), 'utf8'));
  await p.context.TrekMateAPI.request('/admin/dashboard');
  assert.equal(actual.url, 'http://127.0.0.1:5000/api/admin/dashboard');
  assert.equal(actual.options.headers.get('Authorization'), 'Bearer test-token');
});

test('401/403 remove stale sessions; incomplete login responses are rejected', async () => {
  for (const status of [401, 403]) {
    const p = page('index.html', async () => new Response(JSON.stringify({ message: 'Access forbidden' }), { status }));
    p.run(fs.readFileSync(path.join(root, 'js/api.js'), 'utf8'));
    await assert.rejects(p.context.TrekMateAPI.request('/admin/dashboard'));
    assert.equal(p.storage.has('trekmate_admin_token'), false);
    assert.equal(p.context.location.redirect, 'login.html');
  }
  const p = page('login.html');
  p.storage.clear();
  p.run(fs.readFileSync(path.join(root, 'js/api.js'), 'utf8'));
  await assert.rejects(p.context.TrekMateAPI.login('test@trekmate.example', 'test'), /incomplete/);
  assert.equal(p.storage.has('trekmate_admin_token'), false);
});

test('dashboard and user scripts initialize against actual page IDs; stored text is escaped', async () => {
  for (const [filename, script] of [['index.html', 'app.js'], ['users.html', 'users.js']]) {
    const p = page(filename);
    p.context.TrekMateAPI = { requireAuth: () => true, logout() {}, request: async (route) => ({ success: true,
      data: route.includes('monthly') ? { month: '2026-09', summary: {}, providers: [] } : route.endsWith('dashboard') ? {} : [],
    }) };
    p.run(fs.readFileSync(path.join(root, 'js/' + script), 'utf8'));
    p.run(fs.readFileSync(path.join(root, 'js/navigation.js'), 'utf8'));
    await new Promise(resolve => setImmediate(resolve));
    if (filename === 'index.html') {
      p.run(`transactions = [mapTransaction({id: 1, transaction_reference: '<img src=x onerror=alert(1)>',
        customer_name: '<script>bad()</script>', provider_name: '<svg onload=alert(1)>', provider_id: 4,
        created_at: '2026-09-06', gross_amount: 1000, payout_amount: 360, payout_status: 'PENDING',
        payment_status: 'PARTIALLY_REFUNDED', verification_status: 'VERIFIED'})]; renderTransactions(); renderPendingPayouts();`);
      assert.match(p.elements.transactionTableBody.children[0].innerHTML, /&lt;img/);
      assert.doesNotMatch(p.elements.transactionTableBody.children[0].innerHTML, /<script>|<svg|<img/);
      assert.equal(p.elements.pendingPayoutList.children.length, 1);
      assert.doesNotMatch(p.elements.pendingPayoutList.children[0].innerHTML, /<svg/);
      p.run('transactions[0].payoutBatchId = 1; renderPendingPayouts();');
      assert.equal(p.elements.pendingPayoutList.children.length, 0, 'batched payouts must not be offered again');
      p.run('transactions[0].payoutBatchId = null; refunds = [{transaction_id: 1, status: "REQUESTED"}]; renderPendingPayouts();');
      assert.equal(p.elements.pendingPayoutList.children.length, 0, 'refund reservations hold pending settlement');
      p.run(`settlements = [{id:1, settlement_reference:'<img src=x>', provider_name:'<svg>', status:'PENDING', period_start:'2026-09-01', period_end:'2026-09-07'}]; renderSettlements();`);
      assert.doesNotMatch(p.elements.settlementTableBody.children[0].innerHTML, /<img|<svg/);
    } else {
      p.run(`users = [normalise({id:1,full_name:'<img src=x>',email:'<svg>',role:'USER',is_active:1,created_at:'2026-09-06'})]; render();`);
      assert.doesNotMatch(p.elements.userTableBody.innerHTML, /<img|<svg/);
      assert.match(p.elements.userTableBody.innerHTML, /&lt;img/);
    }
  }
});
