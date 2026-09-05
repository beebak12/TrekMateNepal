"use strict";

if (!window.TrekMateAPI || !TrekMateAPI.requireAuth()) throw new Error("Admin authentication is required.");

let users = [];
let selectedUserId = null;
const roleIds = { customer: 1, guide: 2, admin: 3 };
const $ = (id) => document.getElementById(id);
const table = $("userTableBody");
const formModal = $("userFormModal");
const detailsModal = $("providerDetailsModal");
const deactivateModal = $("deactivateModal");

function escapeHTML(value) {
    return String(value ?? "").replaceAll("&", "&amp;").replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;").replaceAll('"', "&quot;").replaceAll("'", "&#039;");
}

function money(value) {
    return `NPR ${Number(value || 0).toLocaleString("en-NP", { maximumFractionDigits: 2 })}`;
}

function date(value) {
    return value ? new Intl.DateTimeFormat("en-GB", {
        day: "2-digit", month: "short", year: "numeric"
    }).format(new Date(value)) : "—";
}

function initials(name) {
    return String(name || "User").split(/\s+/).slice(0, 2).map((part) => part[0]).join("").toUpperCase();
}

function normalise(row) {
    const role = String(row.role || "USER").toUpperCase();
    return {
        ...row,
        name: row.full_name,
        displayRole: role === "USER" ? (Number(row.gear_listed) > 0 ? "provider" : "customer") : role.toLowerCase(),
        status: Number(row.is_active) === 1 ? "active" : "deactivated",
        gearListed: Number(row.gear_listed || 0),
        gearBooked: Number(row.gear_booked || 0),
        earnings: Number(row.provider_earnings || 0)
    };
}

function toast(message, error = false) {
    $("toast").textContent = message;
    $("toast").classList.toggle("error", error);
    $("toast").classList.remove("hidden");
    setTimeout(() => $("toast").classList.add("hidden"), 3000);
}

function busy(button, state, label = "Working…") {
    if (state) { button.dataset.oldLabel = button.textContent; button.textContent = label; }
    else if (button.dataset.oldLabel) button.textContent = button.dataset.oldLabel;
    button.disabled = state;
}

function updateSummary() {
    const providers = users.filter((user) => user.displayRole === "provider");
    $("totalUsers").textContent = users.length;
    $("totalProviders").textContent = providers.length;
    $("totalGearItems").textContent = providers.reduce((sum, user) => sum + user.gearListed, 0);
    $("totalCompletedRentals").textContent = providers.reduce((sum, user) => sum + user.gearBooked, 0);
}

function render() {
    const search = $("userSearch").value.trim().toLowerCase();
    const role = $("roleFilter").value;
    const status = $("userStatusFilter").value;
    const filtered = users.filter((user) => {
        const found = !search || [user.id, user.name, user.username, user.email]
            .some((value) => String(value || "").toLowerCase().includes(search));
        return found && (role === "all" || role === user.displayRole) && (status === "all" || status === user.status);
    });
    table.innerHTML = filtered.map((user) => {
        const provider = user.displayRole === "provider";
        const statusAction = user.status === "active"
            ? `<button class="user-action-button deactivate-button" data-action="deactivate" data-id="${user.id}">Deactivate</button>`
            : `<button class="user-action-button activate-button" data-action="activate" data-id="${user.id}">Activate</button>`;
        return `<tr>
            <td><div class="user-identity"><span class="user-avatar">${escapeHTML(initials(user.name))}</span><div><strong>${escapeHTML(user.name)}</strong><span>#${user.id} · ${escapeHTML(user.email)}</span></div></div></td>
            <td><span class="role-badge role-${escapeHTML(user.displayRole)}">${escapeHTML(user.displayRole)}</span></td>
            <td><span class="status-badge status-${user.status === "active" ? "paid" : "refunded"}">${user.status}</span></td>
            <td>${user.gearListed}</td><td>${user.gearBooked}</td><td>${provider ? money(user.earnings) : "—"}</td>
            <td>${date(user.created_at)}</td><td><div class="user-actions">
            ${provider ? `<button class="user-action-button details-button" data-action="details" data-id="${user.id}">History</button>` : ""}
            <button class="user-action-button" data-action="edit" data-id="${user.id}">Edit</button>${statusAction}</div></td></tr>`;
    }).join("");
    $("userEmptyState").classList.toggle("hidden", filtered.length > 0);
}

async function loadUsers() {
    table.innerHTML = `<tr><td colspan="8">Loading users from MySQL…</td></tr>`;
    try {
        const response = await TrekMateAPI.request("/admin/users");
        users = response.data.map(normalise);
        updateSummary(); render();
    } catch (error) {
        table.innerHTML = `<tr><td colspan="8">${escapeHTML(error.message)}</td></tr>`;
        toast(error.message, true);
    }
}

function openAdd() {
    $("userForm").reset(); $("editingUserId").value = "";
    $("userFormTitle").textContent = "Add New User";
    $("userPasswordGroup").classList.remove("hidden");
    $("userPassword").required = true; $("userStatus").value = "active";
    formModal.classList.remove("hidden"); $("userName").focus();
}

function openEdit(id) {
    const user = users.find((item) => item.id === id); if (!user) return;
    $("editingUserId").value = user.id; $("userFormTitle").textContent = "Edit User";
    $("userName").value = user.name || ""; $("userUsername").value = user.username || "";
    $("userPhone").value = user.phone || ""; $("userEmail").value = user.email || "";
    $("userRole").value = user.displayRole === "provider" ? "customer" : user.displayRole;
    $("userStatus").value = user.status; $("userPassword").required = false;
    $("userPasswordGroup").classList.add("hidden"); formModal.classList.remove("hidden");
}

async function saveUser(event) {
    event.preventDefault();
    const button = $("userForm").querySelector('[type="submit"]');
    const id = Number($("editingUserId").value);
    const payload = {
        full_name: $("userName").value.trim(), username: $("userUsername").value.trim(),
        phone: $("userPhone").value.trim() || null, email: $("userEmail").value.trim(),
        role_id: roleIds[$("userRole").value]
    };
    if (!id) payload.password = $("userPassword").value;
    busy(button, true, "Saving…");
    try {
        let targetId = id;
        if (id) await TrekMateAPI.request(`/admin/users/${id}`, { method: "PUT", body: JSON.stringify(payload) });
        else {
            const result = await TrekMateAPI.request("/admin/users", { method: "POST", body: JSON.stringify(payload) });
            targetId = result.data.id;
        }
        const current = users.find((user) => user.id === id);
        const requestedActive = $("userStatus").value === "active";
        const statusChanged = id ? current && (current.status === "active") !== requestedActive : !requestedActive;
        if (statusChanged) await TrekMateAPI.request(`/admin/users/${targetId}/status`, {
            method: "PATCH", body: JSON.stringify({ is_active: requestedActive })
        });
        formModal.classList.add("hidden"); await loadUsers(); toast(id ? "User updated" : "User created");
    } catch (error) { toast(error.message, true); } finally { busy(button, false); }
}

async function setStatus(id, isActive, button) {
    busy(button, true, isActive ? "Activating…" : "Deactivating…");
    try {
        await TrekMateAPI.request(`/admin/users/${id}/status`, {
            method: "PATCH", body: JSON.stringify({ is_active: isActive })
        });
        deactivateModal.classList.add("hidden"); await loadUsers();
        toast(isActive ? "User activated" : "User deactivated");
    } catch (error) { toast(error.message, true); } finally { busy(button, false); }
}

async function providerDetails(id) {
    detailsModal.classList.remove("hidden"); $("providerDetailsTitle").textContent = "Loading provider…";
    $("providerGearList").innerHTML = ""; $("providerHistoryBody").innerHTML = "";
    try {
        const result = await TrekMateAPI.request(`/admin/providers/${id}/history`);
        const { provider, gear, transactions } = result.data;
        $("providerAvatar").textContent = initials(provider.full_name);
        $("providerDetailsTitle").textContent = provider.full_name;
        $("providerDetailsEmail").textContent = `#${provider.id} · ${provider.email}`;
        $("providerGearCount").textContent = gear.length;
        $("providerBookingCount").textContent = gear.reduce((sum, item) => sum + Number(item.times_booked || 0), 0);
        $("providerEarnings").textContent = money(transactions.reduce((sum, item) => sum + Number(item.provider_payable || 0), 0));
        $("providerGearList").innerHTML = gear.length ? `<div class="provider-gear-heading"><strong>Listed Gear</strong><span>${gear.length} item(s)</span></div>` + gear.map((item) => `<div class="provider-gear-item"><strong>${escapeHTML(item.name)}</strong><span>${money(item.price_per_day)}/day · Qty ${item.quantity} · ${escapeHTML(item.availability)}</span></div>`).join("") : `<div class="provider-gear-empty">No gear currently listed.</div>`;
        $("providerHistoryBody").innerHTML = transactions.map((item) => `<tr><td>${escapeHTML(item.transaction_reference)}</td><td>${item.rental_id ? `Rental #${item.rental_id}` : "No rental link"}</td><td>${escapeHTML(item.customer_name || "—")}</td><td>${date(item.created_at)}</td><td>${money(item.gross_amount)}</td><td>${money(item.provider_payable)}</td><td><span class="status-badge">${escapeHTML(item.payment_status)}</span></td></tr>`).join("");
        $("providerHistoryEmptyState").classList.toggle("hidden", transactions.length > 0);
    } catch (error) { $("providerDetailsTitle").textContent = "Provider unavailable"; toast(error.message, true); }
}

$("userSearch").addEventListener("input", render);
$("roleFilter").addEventListener("change", render);
$("userStatusFilter").addEventListener("change", render);
$("addUserButton").addEventListener("click", openAdd);
$("userForm").addEventListener("submit", saveUser);
$("closeUserFormButton").addEventListener("click", () => formModal.classList.add("hidden"));
$("cancelUserFormButton").addEventListener("click", () => formModal.classList.add("hidden"));
$("closeProviderDetailsButton").addEventListener("click", () => detailsModal.classList.add("hidden"));
$("closeDeactivateButton").addEventListener("click", () => deactivateModal.classList.add("hidden"));
$("cancelDeactivateButton").addEventListener("click", () => deactivateModal.classList.add("hidden"));
$("confirmDeactivateButton").addEventListener("click", (event) => setStatus(selectedUserId, false, event.currentTarget));
$("providerNavigation").addEventListener("click", () => { $("roleFilter").value = "provider"; render(); });
table.addEventListener("click", (event) => {
    const button = event.target.closest("button[data-action]"); if (!button) return;
    const id = Number(button.dataset.id);
    if (button.dataset.action === "edit") openEdit(id);
    if (button.dataset.action === "details") providerDetails(id);
    if (button.dataset.action === "activate") setStatus(id, true, button);
    if (button.dataset.action === "deactivate") {
        selectedUserId = id; $("deactivateUserName").textContent = users.find((user) => user.id === id).name;
        deactivateModal.classList.remove("hidden");
    }
});

[formModal, detailsModal, deactivateModal].forEach((modal) => modal.addEventListener("click", (event) => {
    if (event.target === modal) modal.classList.add("hidden");
}));
$("menuButton").addEventListener("click", () => $("sidebar").classList.toggle("open"));
document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") [formModal, detailsModal, deactivateModal].forEach((modal) => modal.classList.add("hidden"));
});

loadUsers();
