"use strict";

/* Fallback data is retained only as an empty collection. */

let transactions = [];
let dashboardSummary = null;

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

        const actionDisabled = true;

        let actionText = "View";

        if (transaction.payoutStatus === "pending") {
            actionText = "Weekly batch";
        }

        if (transaction.payoutStatus === "paid") {
            actionText = "Completed";
        }

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
                    class="table-action"
                    data-payout-id="${transaction.id}"
                    ${actionDisabled ? "disabled" : ""}
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

    document.querySelectorAll(
        ".table-action:not(:disabled)"
    ).forEach((button) => {
        button.addEventListener("click", () => {
            openPayoutModal(button.dataset.payoutId);
        });
    });
}

/* Render pending payout list */

function renderPendingPayouts() {
    const pendingTransactions = transactions.filter(
        (transaction) =>
            transaction.paymentStatus === "verified" &&
            transaction.payoutStatus === "pending"
    );

    pendingPayoutList.innerHTML = "";
    pendingPayoutCount.textContent =
        pendingTransactions.length;

    if (pendingTransactions.length === 0) {
        pendingPayoutList.innerHTML = `
            <div class="empty-state">
                <strong>No pending payouts</strong>
                <p>All provider payouts have been completed.</p>
            </div>
        `;

        return;
    }

    pendingTransactions.forEach((transaction) => {
        const payoutItem = document.createElement("div");
        payoutItem.className = "payout-item";

        payoutItem.innerHTML = `
            <div class="provider-avatar">
                ${createInitials(transaction.provider)}
            </div>

            <div class="payout-information">
                <strong>${transaction.provider}</strong>
                <span>
                    ${transaction.id} · ${transaction.date}
                </span>
            </div>

            <div class="payout-action">
                <strong>
                    ${formatCurrency(
                        calculateProviderShare(
                            transaction.grossAmount
                        )
                    )}
                </strong>

                <button
                    class="approve-button"
                    data-payout-id="${transaction.id}"
                    disabled
                >
                    Add through weekly settlement
                </button>
            </div>
        `;

        pendingPayoutList.appendChild(payoutItem);
    });

    document.querySelectorAll(
        ".approve-button"
    ).forEach((button) => {
        button.addEventListener("click", () => {
            openPayoutModal(button.dataset.payoutId);
        });
    });
}

/* Open payout confirmation window */

function openPayoutModal(transactionId) {
    const transaction = transactions.find(
        (item) => item.id === transactionId
    );

    if (!transaction) {
        return;
    }

    selectedPayoutId = transactionId;

    modalProviderName.textContent =
        transaction.provider;

    modalPayoutAmount.textContent = formatCurrency(
        calculateProviderShare(transaction.grossAmount)
    );

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

function confirmPayout() {
    const transaction = transactions.find(
        (item) => item.id === selectedPayoutId
    );

    if (!transaction) {
        return;
    }

    /*
     * Prototype behaviour only.
     *
     * In the final system, this should call an authenticated
     * Express API endpoint. The backend should update MySQL
     * only after the administrator confirms the manual transfer.
     */

    transaction.payoutStatus = "paid";

    closePayoutModal();
    updateDashboard();
    showToast(
        `${transaction.provider} payout marked as paid.`
    );
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
        const [dashboardResponse, transactionResponse] = await Promise.all([
            TrekMateAPI.request("/admin/dashboard"),
            TrekMateAPI.request("/admin/transactions")
        ]);

        dashboardSummary = dashboardResponse.data;
        transactions = (transactionResponse.data || []).map(mapTransaction);
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

document.addEventListener("keydown", (event) => {
    if (
        event.key === "Escape" &&
        !payoutModal.classList.contains("hidden")
    ) {
        closePayoutModal();
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
