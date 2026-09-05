"use strict";

/* Fallback data is retained only as an empty collection. */

let transactions = [];
let dashboardSummary = null;
let settlements = [];

/*
const demonstrationTransactions = [
    {
        id: "TXN-1001",
        bookingId: "BK-2101",
        date: "30 Aug 2026",
        customer: "Aarav Sharma",
        customerEmail: "aarav@example.com",
        provider: "Mountain Gear Hub",
        providerEmail: "mountaingear@example.com",
        grossAmount: 10000,
        paymentStatus: "verified",
        payoutStatus: "pending"
    },
    {
        id: "TXN-1002",
        bookingId: "BK-2102",
        date: "29 Aug 2026",
        customer: "Nisha Thapa",
        customerEmail: "nisha@example.com",
        provider: "Himalayan Rentals",
        providerEmail: "himalayan@example.com",
        grossAmount: 25000,
        paymentStatus: "verified",
        payoutStatus: "paid"
    },
    {
        id: "TXN-1003",
        bookingId: "BK-2103",
        date: "29 Aug 2026",
        customer: "Rohan Gurung",
        customerEmail: "rohan@example.com",
        provider: "Everest Trek Store",
        providerEmail: "evereststore@example.com",
        grossAmount: 15000,
        paymentStatus: "verified",
        payoutStatus: "pending"
    },
    {
        id: "TXN-1004",
        bookingId: "BK-2104",
        date: "28 Aug 2026",
        customer: "Sanjana Rai",
        customerEmail: "sanjana@example.com",
        provider: "Pokhara Outdoor Gear",
        providerEmail: "pokharaoutdoor@example.com",
        grossAmount: 30000,
        paymentStatus: "verified",
        payoutStatus: "paid"
    },
    {
        id: "TXN-1005",
        bookingId: "BK-2105",
        date: "28 Aug 2026",
        customer: "Bikash Karki",
        customerEmail: "bikash@example.com",
        provider: "Annapurna Equipment",
        providerEmail: "annapurnaequipment@example.com",
        grossAmount: 5000,
        paymentStatus: "refunded",
        payoutStatus: "unpaid"
    },
    {
        id: "TXN-1006",
        bookingId: "BK-2106",
        date: "27 Aug 2026",
        customer: "Isha Adhikari",
        customerEmail: "isha@example.com",
        provider: "Kathmandu Trek Supply",
        providerEmail: "ktmtrek@example.com",
        grossAmount: 40000,
        paymentStatus: "verified",
        payoutStatus: "paid"
    },
    {
        id: "TXN-1007",
        bookingId: "BK-2107",
        date: "27 Aug 2026",
        customer: "Manish Tamang",
        customerEmail: "manish@example.com",
        provider: "Langtang Gear Point",
        providerEmail: "langtanggear@example.com",
        grossAmount: 20000,
        paymentStatus: "verified",
        payoutStatus: "pending"
    }
];
*/

/* Percentage used by the TrekMate revenue model */

const COMMISSION_RATE = 0.10;
const PROVIDER_RATE = 0.90;

/* Selected payout is stored here while the modal is open */

let selectedPayoutId = null;
let selectedVerificationId = null;
let selectedSettlementId = null;

/* HTML elements */

const transactionTableBody = document.getElementById(
    "transactionTableBody"
);

const pendingPayoutList = document.getElementById(
    "pendingPayoutList"
);

const pendingPayoutCount = document.getElementById(
    "pendingPayoutCount"
);

const transactionSearch = document.getElementById(
    "transactionSearch"
);

const statusFilter = document.getElementById(
    "statusFilter"
);

const emptyState = document.getElementById(
    "emptyState"
);

const payoutModal = document.getElementById(
    "payoutModal"
);

const modalProviderName = document.getElementById(
    "modalProviderName"
);

const modalPayoutAmount = document.getElementById(
    "modalPayoutAmount"
);

const confirmPayoutButton = document.getElementById(
    "confirmPayoutButton"
);

const cancelPayoutButton = document.getElementById(
    "cancelPayoutButton"
);

const modalCloseButton = document.getElementById(
    "modalCloseButton"
);

const toast = document.getElementById("toast");
const menuButton = document.getElementById("menuButton");
const sidebar = document.getElementById("sidebar");
const verificationModal = document.getElementById("verificationModal");
const verificationReference = document.getElementById("verificationReference");
const verificationGatewayId = document.getElementById("verificationGatewayId");
const confirmVerificationButton = document.getElementById("confirmVerificationButton");
const markFailedButton = document.getElementById("markFailedButton");
const settlementTableBody = document.getElementById("settlementTableBody");
const settlementStartDate = document.getElementById("settlementStartDate");
const settlementEndDate = document.getElementById("settlementEndDate");
const settlementPaidModal = document.getElementById("settlementPaidModal");
const settlementPayoutReference = document.getElementById("settlementPayoutReference");
const confirmSettlementPaidButton = document.getElementById("confirmSettlementPaidButton");

/* Format numbers as Nepalese rupees */

function formatCurrency(amount) {
    const numericAmount = Number(amount || 0);
    return `NPR ${new Intl.NumberFormat("en-IN", {
        minimumFractionDigits: 0,
        maximumFractionDigits: 2
    }).format(numericAmount)}`;
}

/* Calculate commission */

function calculateCommission(grossAmount) {
    return grossAmount * COMMISSION_RATE;
}

/* Calculate provider share */

function calculateProviderShare(grossAmount) {
    return grossAmount * PROVIDER_RATE;
}

/* Create initials for provider avatar */

function createInitials(name) {
    return name
        .split(" ")
        .slice(0, 2)
        .map((word) => word.charAt(0))
        .join("")
        .toUpperCase();
}

/* Create status badge */

function createStatusBadge(status) {
    return `
        <span class="status-badge status-${status}">
            ${status}
        </span>
    `;
}

/* Update financial summary using the current records */

function updateFinancialSummary() {
    if (dashboardSummary) {
        document.getElementById(
            "totalBookingAmount"
        ).textContent = formatCurrency(
            dashboardSummary.total_booking_amount
        );

        document.getElementById(
            "totalCommission"
        ).textContent = formatCurrency(
            dashboardSummary.trekmate_revenue
        );

        document.getElementById(
            "pendingPayables"
        ).textContent = formatCurrency(
            dashboardSummary.pending_payables
        );

        document.getElementById(
            "totalRefunds"
        ).textContent = formatCurrency(
            dashboardSummary.total_refunds
        );

        const liveRevenueValues = document.querySelectorAll(
            ".revenue-detail strong"
        );

        if (liveRevenueValues.length >= 2) {
            liveRevenueValues[0].textContent = formatCurrency(
                dashboardSummary.trekmate_revenue
            );
            liveRevenueValues[1].textContent = formatCurrency(
                Number(dashboardSummary.total_booking_amount) -
                    Number(dashboardSummary.trekmate_revenue)
            );
        }

        return;
    }

    const verifiedTransactions = transactions.filter(
        (transaction) =>
            transaction.paymentStatus === "verified"
    );

    const refundedTransactions = transactions.filter(
        (transaction) =>
            transaction.paymentStatus === "refunded"
    );

    const pendingTransactions = verifiedTransactions.filter(
        (transaction) =>
            transaction.payoutStatus === "pending"
    );

    const verifiedGross = verifiedTransactions.reduce(
        (total, transaction) =>
            total + transaction.grossAmount,
        0
    );

    const totalCommission = verifiedTransactions.reduce(
        (total, transaction) =>
            total + calculateCommission(transaction.grossAmount),
        0
    );

    const totalProviderShare = verifiedTransactions.reduce(
        (total, transaction) =>
            total + calculateProviderShare(transaction.grossAmount),
        0
    );

    const pendingPayables = pendingTransactions.reduce(
        (total, transaction) =>
            total + calculateProviderShare(transaction.grossAmount),
        0
    );

    const totalRefunds = refundedTransactions.reduce(
        (total, transaction) =>
            total + transaction.grossAmount,
        0
    );

    document.getElementById(
        "totalBookingAmount"
    ).textContent = formatCurrency(verifiedGross);

    document.getElementById(
        "totalCommission"
    ).textContent = formatCurrency(totalCommission);

    document.getElementById(
        "pendingPayables"
    ).textContent = formatCurrency(pendingPayables);

    document.getElementById(
        "totalRefunds"
    ).textContent = formatCurrency(totalRefunds);

    /*
     * Update the two values displayed beside the commission chart.
     */

    const revenueValues = document.querySelectorAll(
        ".revenue-detail strong"
    );

    if (revenueValues.length >= 2) {
        revenueValues[0].textContent =
            formatCurrency(totalCommission);

        revenueValues[1].textContent =
            formatCurrency(totalProviderShare);
    }
}

/* Render transaction table */

function renderTransactions() {
    const searchValue = transactionSearch.value
        .trim()
        .toLowerCase();

    const selectedStatus = statusFilter.value;

    const filteredTransactions = transactions.filter(
        (transaction) => {
            const searchableText = `
                ${transaction.id}
                ${transaction.bookingId}
                ${transaction.customer}
                ${transaction.provider}
            `.toLowerCase();

            const matchesSearch =
                searchableText.includes(searchValue);

            const matchesStatus =
                selectedStatus === "all" ||
                transaction.paymentStatus === selectedStatus;

            return matchesSearch && matchesStatus;
        }
    );

    transactionTableBody.innerHTML = "";

    filteredTransactions.forEach((transaction) => {
        const commission = transaction.commissionAmount ??
            calculateCommission(transaction.grossAmount);

        const providerShare = transaction.providerShare ??
            calculateProviderShare(transaction.grossAmount);

        const canVerify = transaction.paymentStatus === "pending" ||
            transaction.paymentStatus === "failed";

        let actionText = canVerify ? "Verify" : "Verified";

        if (transaction.paymentStatus === "refunded") {
            actionText = "Refunded";
        }

        const row = document.createElement("tr");

        row.innerHTML = `
            <td class="transaction-id">
                <strong>${transaction.id}</strong>
                <span>
                    ${transaction.bookingId} · ${transaction.date}
                </span>
            </td>

            <td class="person-cell">
                <strong>${transaction.customer}</strong>
                <span>${transaction.customerEmail}</span>
            </td>

            <td class="person-cell">
                <strong>${transaction.provider}</strong>
                <span>${transaction.providerEmail}</span>
            </td>

            <td class="amount-cell">
                ${formatCurrency(transaction.grossAmount)}
            </td>

            <td class="commission-cell">
                ${formatCurrency(commission)}
            </td>

            <td class="amount-cell">
                ${formatCurrency(providerShare)}
            </td>

            <td>
                ${createStatusBadge(transaction.paymentStatus)}
            </td>

            <td>
                ${createStatusBadge(transaction.payoutStatus)}
            </td>

            <td>
                <button
                    class="table-action verify-transaction-button"
                    data-transaction-id="${transaction.databaseId}"
                    ${canVerify ? "" : "disabled"}
                >
                    ${actionText}
                </button>
            </td>
        `;

        transactionTableBody.appendChild(row);
    });

    emptyState.classList.toggle(
        "hidden",
        filteredTransactions.length !== 0
    );

    document.querySelectorAll(".verify-transaction-button:not(:disabled)")
        .forEach((button) => button.addEventListener("click", () => {
            openVerificationModal(Number(button.dataset.transactionId));
        }));
}

function openVerificationModal(databaseId) {
    const transaction = transactions.find((item) => item.databaseId === databaseId);
    if (!transaction) return;
    selectedVerificationId = databaseId;
    verificationReference.textContent = transaction.id;
    verificationGatewayId.value = "";
    verificationModal.classList.remove("hidden");
    document.body.style.overflow = "hidden";
    verificationGatewayId.focus();
}

function closeVerificationModal() {
    selectedVerificationId = null;
    verificationModal.classList.add("hidden");
    document.body.style.overflow = "";
}

function setActionBusy(button, isBusy, busyText) {
    if (isBusy) {
        button.dataset.originalText = button.textContent;
        button.textContent = busyText;
    } else if (button.dataset.originalText) {
        button.textContent = button.dataset.originalText;
    }
    button.disabled = isBusy;
}

async function updateVerification(verified, button) {
    if (!selectedVerificationId) return;
    const gatewayId = verificationGatewayId.value.trim();
    if (verified && !gatewayId) {
        showToast("Enter the sandbox or gateway transaction ID.");
        verificationGatewayId.focus();
        return;
    }
    setActionBusy(button, true, verified ? "Verifying…" : "Saving…");
    try {
        await TrekMateAPI.request(`/admin/transactions/${selectedVerificationId}/verify`, {
            method: "PATCH",
            body: JSON.stringify({
                verified,
                gateway_transaction_id: gatewayId || null
            })
        });
        closeVerificationModal();
        await loadDashboardData();
        showToast(verified ? "Transaction verified." : "Transaction marked failed.");
    } catch (error) {
        showToast(error.message);
    } finally {
        setActionBusy(button, false);
    }
}

/* Render pending payout list */

function renderPendingPayouts() {
    const pendingTransactions = transactions.filter(
        (transaction) =>
            transaction.paymentStatus === "verified" &&
            transaction.payoutStatus === "pending"
    );
    const pendingProviders = Array.from(
        pendingTransactions.reduce((providers, transaction) => {
            const current = providers.get(transaction.providerId) || {
                providerId: transaction.providerId,
                provider: transaction.provider,
                transactionCount: 0,
                amount: 0
            };
            current.transactionCount += 1;
            current.amount += transaction.providerShare;
            providers.set(transaction.providerId, current);
            return providers;
        }, new Map()).values()
    );

    pendingPayoutList.innerHTML = "";
    pendingPayoutCount.textContent =
        pendingProviders.length;

    if (pendingTransactions.length === 0) {
        pendingPayoutList.innerHTML = `
            <div class="empty-state">
                <strong>No pending payouts</strong>
                <p>All provider payouts have been completed.</p>
            </div>
        `;

        return;
    }

    pendingProviders.forEach((provider) => {
        const payoutItem = document.createElement("div");
        payoutItem.className = "payout-item";

        payoutItem.innerHTML = `
            <div class="provider-avatar">
                ${createInitials(provider.provider)}
            </div>

            <div class="payout-information">
                <strong>${provider.provider}</strong>
                <span>
                    ${provider.transactionCount} verified transaction(s)
                </span>
            </div>

            <div class="payout-action">
                <strong>
                    ${formatCurrency(
                        provider.amount
                    )}
                </strong>

                <button
                    class="approve-button"
                    data-provider-id="${provider.providerId}"
                >
                    Create weekly settlement
                </button>
            </div>
        `;

        pendingPayoutList.appendChild(payoutItem);
    });

    document.querySelectorAll(
        ".approve-button"
    ).forEach((button) => {
        button.addEventListener("click", () => {
            openPayoutModal(Number(button.dataset.providerId));
        });
    });
}

/* Open payout confirmation window */

function formatDateInput(date) {
    return date.toISOString().slice(0, 10);
}

function openPayoutModal(providerId) {
    const providerTransactions = transactions.filter(
        (item) => item.providerId === providerId && item.paymentStatus === "verified" && item.payoutStatus === "pending"
    );
    const transaction = providerTransactions[0];

    if (!transaction) {
        return;
    }

    selectedPayoutId = providerId;

    modalProviderName.textContent =
        transaction.provider;

    modalPayoutAmount.textContent = formatCurrency(
        providerTransactions.reduce((sum, item) => sum + item.providerShare, 0)
    );

    const end = new Date();
    const start = new Date(end);
    start.setDate(end.getDate() - 6);
    settlementStartDate.value = formatDateInput(start);
    settlementEndDate.value = formatDateInput(end);

    payoutModal.classList.remove("hidden");
    document.body.style.overflow = "hidden";
}

/* Close payout confirmation window */

function closePayoutModal() {
    selectedPayoutId = null;
    payoutModal.classList.add("hidden");
    document.body.style.overflow = "";
}

/* Mark the selected demonstration payout as paid */

async function confirmPayout() {
    if (!selectedPayoutId) return;
    const start = settlementStartDate.value;
    const end = settlementEndDate.value;
    if (!start || !end) return showToast("Select both settlement dates.");
    const days = Math.floor((new Date(end) - new Date(start)) / 86400000);
    if (days < 0 || days > 6) return showToast("Settlement period must be one to seven days.");
    setActionBusy(confirmPayoutButton, true, "Creating…");
    try {
        await TrekMateAPI.request("/admin/settlements", {
            method: "POST",
            body: JSON.stringify({ provider_id: selectedPayoutId, period_start: start, period_end: end })
        });
        closePayoutModal();
        await loadDashboardData();
        showToast("Weekly settlement created.");
    } catch (error) {
        showToast(error.message);
    } finally {
        setActionBusy(confirmPayoutButton, false);
    }
}

function renderSettlements() {
    settlementTableBody.innerHTML = "";
    if (!settlements.length) {
        settlementTableBody.innerHTML = '<tr><td colspan="7">No weekly settlements yet.</td></tr>';
        return;
    }
    settlements.forEach((settlement) => {
        const row = document.createElement("tr");
        const status = String(settlement.status || "PENDING").toLowerCase();
        let action = '<span>Completed</span>';
        if (status === "pending") action = `<button class="settlement-action" data-action="approve" data-id="${settlement.id}">Approve</button>`;
        if (status === "approved") action = `<button class="settlement-action" data-action="paid" data-id="${settlement.id}">Mark Paid</button>`;
        if (status === "rejected") action = '<span>Rejected</span>';
        row.innerHTML = `
            <td><strong>${settlement.settlement_reference}</strong></td>
            <td>${settlement.provider_name || settlement.provider_email || `Provider #${settlement.provider_id}`}</td>
            <td>${settlement.period_start} – ${settlement.period_end}</td>
            <td>${settlement.transaction_count || 0}</td>
            <td><strong>${formatCurrency(settlement.total_amount)}</strong></td>
            <td>${createStatusBadge(status)}</td>
            <td>${action}</td>`;
        settlementTableBody.appendChild(row);
    });
    settlementTableBody.querySelectorAll(".settlement-action").forEach((button) => {
        button.addEventListener("click", () => handleSettlementAction(button));
    });
}

async function handleSettlementAction(button) {
    const id = Number(button.dataset.id);
    if (button.dataset.action === "paid") {
        selectedSettlementId = id;
        settlementPayoutReference.value = "";
        settlementPaidModal.classList.remove("hidden");
        document.body.style.overflow = "hidden";
        settlementPayoutReference.focus();
        return;
    }
    setActionBusy(button, true, "Approving…");
    try {
        await TrekMateAPI.request(`/admin/settlements/${id}`, { method: "PATCH", body: JSON.stringify({ status: "APPROVED" }) });
        await loadDashboardData();
        showToast("Settlement approved.");
    } catch (error) { showToast(error.message); }
    finally { setActionBusy(button, false); }
}

function closeSettlementPaidModal() {
    selectedSettlementId = null;
    settlementPaidModal.classList.add("hidden");
    document.body.style.overflow = "";
}

async function markSettlementPaid() {
    const reference = settlementPayoutReference.value.trim();
    if (!reference) return showToast("Enter the payout reference.");
    setActionBusy(confirmSettlementPaidButton, true, "Saving…");
    try {
        await TrekMateAPI.request(`/admin/settlements/${selectedSettlementId}`, {
            method: "PATCH",
            body: JSON.stringify({ status: "PAID", payout_reference: reference })
        });
        closeSettlementPaidModal();
        await loadDashboardData();
        showToast("Settlement marked paid.");
    } catch (error) { showToast(error.message); }
    finally { setActionBusy(confirmSettlementPaidButton, false); }
}

/* Display temporary notification */

function showToast(message) {
    toast.textContent = message;
    toast.classList.remove("hidden");

    window.setTimeout(() => {
        toast.classList.add("hidden");
    }, 3000);
}

/* Update all dashboard areas */

function updateDashboard() {
    updateFinancialSummary();
    renderTransactions();
    renderPendingPayouts();
    renderSettlements();
}

function normalisePaymentStatus(transaction) {
    if (
        transaction.payment_status === "REFUNDED" ||
        transaction.payment_status === "PARTIALLY_REFUNDED"
    ) {
        return "refunded";
    }

    if (transaction.verification_status === "VERIFIED") {
        return "verified";
    }

    if (transaction.verification_status === "FAILED") {
        return "failed";
    }

    return "pending";
}

function mapTransaction(transaction) {
    const bookingReference = transaction.rental_id
        ? `Rental #${transaction.rental_id}`
        : transaction.package_booking_id
            ? `Package #${transaction.package_booking_id}`
            : "No booking link";

    return {
        id: transaction.transaction_reference || `TXN-${transaction.id}`,
        databaseId: transaction.id,
        bookingId: bookingReference,
        date: new Date(transaction.created_at).toLocaleDateString(
            "en-GB",
            { day: "2-digit", month: "short", year: "numeric" }
        ),
        customer: transaction.customer_name || "Unknown customer",
        customerEmail: transaction.customer_email || "—",
        provider: transaction.provider_name || "TrekMate",
        providerId: Number(transaction.provider_id),
        providerEmail: transaction.provider_email || "—",
        grossAmount: Number(transaction.gross_amount || 0),
        commissionAmount: Number(transaction.commission_amount || 0),
        providerShare: Number(transaction.provider_payable || 0),
        paymentStatus: normalisePaymentStatus(transaction),
        payoutStatus: String(transaction.payout_status || "unpaid").toLowerCase()
    };
}

async function loadDashboardData() {
    if (!TrekMateAPI.requireAuth()) {
        return;
    }

    transactionTableBody.innerHTML = `
        <tr><td colspan="9">Loading live transaction data…</td></tr>
    `;

    try {
        const [dashboardResponse, transactionResponse, settlementResponse] = await Promise.all([
            TrekMateAPI.request("/admin/dashboard"),
            TrekMateAPI.request("/admin/transactions"),
            TrekMateAPI.request("/admin/settlements")
        ]);

        dashboardSummary = dashboardResponse.data;
        transactions = (transactionResponse.data || []).map(mapTransaction);
        settlements = settlementResponse.data || [];
        updateDashboard();
    } catch (error) {
        transactionTableBody.innerHTML = `
            <tr><td colspan="9">Unable to load live transaction data.</td></tr>
        `;
        showToast(error.message);
    }
}

/* Search and filter events */

transactionSearch.addEventListener(
    "input",
    renderTransactions
);

statusFilter.addEventListener(
    "change",
    renderTransactions
);

/* Modal events */

confirmPayoutButton.addEventListener(
    "click",
    confirmPayout
);

cancelPayoutButton.addEventListener(
    "click",
    closePayoutModal
);

modalCloseButton.addEventListener(
    "click",
    closePayoutModal
);

payoutModal.addEventListener("click", (event) => {
    if (event.target === payoutModal) {
        closePayoutModal();
    }
});

document.getElementById("closeVerificationButton").addEventListener("click", closeVerificationModal);
document.getElementById("cancelVerificationButton").addEventListener("click", closeVerificationModal);
confirmVerificationButton.addEventListener("click", () => updateVerification(true, confirmVerificationButton));
markFailedButton.addEventListener("click", () => updateVerification(false, markFailedButton));
verificationModal.addEventListener("click", (event) => {
    if (event.target === verificationModal) closeVerificationModal();
});
document.getElementById("closeSettlementPaidButton").addEventListener("click", closeSettlementPaidModal);
document.getElementById("cancelSettlementPaidButton").addEventListener("click", closeSettlementPaidModal);
confirmSettlementPaidButton.addEventListener("click", markSettlementPaid);
settlementPaidModal.addEventListener("click", (event) => {
    if (event.target === settlementPaidModal) closeSettlementPaidModal();
});

document.addEventListener("keydown", (event) => {
    if (
        event.key === "Escape" &&
        !payoutModal.classList.contains("hidden")
    ) {
        closePayoutModal();
    }

    if (event.key === "Escape" && !verificationModal.classList.contains("hidden")) {
        closeVerificationModal();
    }
    if (event.key === "Escape" && !settlementPaidModal.classList.contains("hidden")) {
        closeSettlementPaidModal();
    }
});

/* Mobile navigation */

menuButton.addEventListener("click", () => {
    sidebar.classList.toggle("open");
});

/* Sidebar navigation behaviour */

document.querySelectorAll(
    ".navigation-item"
).forEach((button) => {
    button.addEventListener("click", () => {
        document.querySelectorAll(
            ".navigation-item"
        ).forEach((item) => {
            item.classList.remove("active");
        });

        button.classList.add("active");

        const section = button.dataset.section;

        if (
            section === "transactions" ||
            section === "refunds"
        ) {
            document
                .querySelector(".transaction-panel")
                .scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });

            statusFilter.value =
                section === "refunds"
                    ? "refunded"
                    : "all";

            renderTransactions();
        }

        if (section === "revenue") {
            document
                .querySelector(".revenue-overview")
                .scrollIntoView({
                    behavior: "smooth",
                    block: "center"
                });
        }

        if (section === "payouts") {
            document
                .querySelector(".pending-panel")
                .scrollIntoView({
                    behavior: "smooth",
                    block: "center"
                });
        }

        if (section === "dashboard") {
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });

            statusFilter.value = "all";
            renderTransactions();
        }

        sidebar.classList.remove("open");
    });
});

/* Initial dashboard display */

loadDashboardData();
