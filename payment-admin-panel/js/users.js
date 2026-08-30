"use strict";

/*
 * Demonstration user and rental data.
 *
 * Bibek can later replace this data with responses from
 * the Node.js and Express.js API.
 */

const users = [
    {
        id: "USR-1001",
        name: "Aarav Sharma",
        email: "aarav@example.com",
        role: "customer",
        status: "active",
        joinedDate: "10 Aug 2026",
        gearListed: 0,
        rentalHistory: []
    },
    {
        id: "PRV-2001",
        name: "Mountain Gear Hub",
        email: "mountaingear@example.com",
        role: "provider",
        status: "active",
        joinedDate: "03 Jul 2026",
        gearListed: 8,
        rentalHistory: [
            {
                bookingId: "BK-2101",
                gearItem: "Four-Person Trekking Tent",
                customer: "Aarav Sharma",
                rentalPeriod: "25–28 Aug 2026",
                amount: 10000,
                status: "completed"
            },
            {
                bookingId: "BK-2084",
                gearItem: "Down Sleeping Bag",
                customer: "Nisha Thapa",
                rentalPeriod: "15–18 Aug 2026",
                amount: 6000,
                status: "completed"
            }
        ]
    },
    {
        id: "USR-1002",
        name: "Nisha Thapa",
        email: "nisha@example.com",
        role: "customer",
        status: "active",
        joinedDate: "14 Jul 2026",
        gearListed: 0,
        rentalHistory: []
    },
    {
        id: "PRV-2002",
        name: "Himalayan Rentals",
        email: "himalayan@example.com",
        role: "provider",
        status: "active",
        joinedDate: "22 Jun 2026",
        gearListed: 12,
        rentalHistory: [
            {
                bookingId: "BK-2102",
                gearItem: "Complete Annapurna Gear Set",
                customer: "Nisha Thapa",
                rentalPeriod: "20–26 Aug 2026",
                amount: 25000,
                status: "completed"
            },
            {
                bookingId: "BK-2070",
                gearItem: "Trekking Poles",
                customer: "Bikash Karki",
                rentalPeriod: "10–12 Aug 2026",
                amount: 4000,
                status: "completed"
            },
            {
                bookingId: "BK-2055",
                gearItem: "Waterproof Trekking Boots",
                customer: "Sanjana Rai",
                rentalPeriod: "02–07 Aug 2026",
                amount: 9000,
                status: "completed"
            }
        ]
    },
    {
        id: "PRV-2003",
        name: "Everest Trek Store",
        email: "evereststore@example.com",
        role: "provider",
        status: "active",
        joinedDate: "18 Jun 2026",
        gearListed: 6,
        rentalHistory: [
            {
                bookingId: "BK-2103",
                gearItem: "Everest Base Camp Gear Package",
                customer: "Rohan Gurung",
                rentalPeriod: "27 Aug–03 Sep 2026",
                amount: 15000,
                status: "active"
            },
            {
                bookingId: "BK-2036",
                gearItem: "Insulated Trekking Jacket",
                customer: "Manish Tamang",
                rentalPeriod: "22–25 Jul 2026",
                amount: 7000,
                status: "completed"
            }
        ]
    },
    {
        id: "USR-1003",
        name: "Rohan Gurung",
        email: "rohan@example.com",
        role: "customer",
        status: "active",
        joinedDate: "02 Jul 2026",
        gearListed: 0,
        rentalHistory: []
    },
    {
        id: "PRV-2004",
        name: "Annapurna Equipment",
        email: "annapurnaequipment@example.com",
        role: "provider",
        status: "deactivated",
        joinedDate: "07 May 2026",
        gearListed: 4,
        rentalHistory: [
            {
                bookingId: "BK-2018",
                gearItem: "Trekking Backpack 65L",
                customer: "Isha Adhikari",
                rentalPeriod: "12–16 Jul 2026",
                amount: 5000,
                status: "refunded"
            }
        ]
    },
    {
        id: "USR-1004",
        name: "Sanjana Rai",
        email: "sanjana@example.com",
        role: "customer",
        status: "deactivated",
        joinedDate: "28 May 2026",
        gearListed: 0,
        rentalHistory: []
    }
];

const PROVIDER_RATE = 0.90;

let selectedUserId = null;

/* HTML references */

const userTableBody =
    document.getElementById("userTableBody");

const userSearch =
    document.getElementById("userSearch");

const roleFilter =
    document.getElementById("roleFilter");

const userStatusFilter =
    document.getElementById("userStatusFilter");

const userEmptyState =
    document.getElementById("userEmptyState");

const addUserButton =
    document.getElementById("addUserButton");

const userFormModal =
    document.getElementById("userFormModal");

const userForm =
    document.getElementById("userForm");

const userFormTitle =
    document.getElementById("userFormTitle");

const editingUserId =
    document.getElementById("editingUserId");

const userName =
    document.getElementById("userName");

const userEmail =
    document.getElementById("userEmail");

const userRole =
    document.getElementById("userRole");

const userStatus =
    document.getElementById("userStatus");

const closeUserFormButton =
    document.getElementById("closeUserFormButton");

const cancelUserFormButton =
    document.getElementById("cancelUserFormButton");

const providerDetailsModal =
    document.getElementById("providerDetailsModal");

const closeProviderDetailsButton =
    document.getElementById("closeProviderDetailsButton");

const deactivateModal =
    document.getElementById("deactivateModal");

const deactivateUserName =
    document.getElementById("deactivateUserName");

const closeDeactivateButton =
    document.getElementById("closeDeactivateButton");

const cancelDeactivateButton =
    document.getElementById("cancelDeactivateButton");

const confirmDeactivateButton =
    document.getElementById("confirmDeactivateButton");

const providerNavigation =
    document.getElementById("providerNavigation");

const toast =
    document.getElementById("toast");

const menuButton =
    document.getElementById("menuButton");

const sidebar =
    document.getElementById("sidebar");

/* Utility functions */

function formatCurrency(amount) {
    return `NPR ${new Intl.NumberFormat("en-IN").format(amount)}`;
}

function createInitials(name) {
    return name
        .split(" ")
        .slice(0, 2)
        .map((word) => word.charAt(0))
        .join("")
        .toUpperCase();
}

function escapeHTML(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function calculateProviderEarnings(user) {
    return user.rentalHistory
        .filter((rental) => rental.status === "completed")
        .reduce(
            (total, rental) =>
                total + rental.amount * PROVIDER_RATE,
            0
        );
}

function calculateCompletedRentals(user) {
    return user.rentalHistory.filter(
        (rental) => rental.status === "completed"
    ).length;
}

/* Dashboard summary */

function updateUserSummary() {
    const providers = users.filter(
        (user) => user.role === "provider"
    );

    const totalGear = providers.reduce(
        (total, provider) =>
            total + provider.gearListed,
        0
    );

    const completedRentals = providers.reduce(
        (total, provider) =>
            total + calculateCompletedRentals(provider),
        0
    );

    document.getElementById(
        "totalUsers"
    ).textContent = users.length;

    document.getElementById(
        "totalProviders"
    ).textContent = providers.length;

    document.getElementById(
        "totalGearItems"
    ).textContent = totalGear;

    document.getElementById(
        "totalCompletedRentals"
    ).textContent = completedRentals;
}

/* User table */

function renderUsers() {
    const searchValue = userSearch.value
        .trim()
        .toLowerCase();

    const selectedRole = roleFilter.value;
    const selectedStatus = userStatusFilter.value;

    const filteredUsers = users.filter((user) => {
        const searchableText = `
            ${user.id}
            ${user.name}
            ${user.email}
        `.toLowerCase();

        const matchesSearch =
            searchableText.includes(searchValue);

        const matchesRole =
            selectedRole === "all" ||
            user.role === selectedRole;

        const matchesStatus =
            selectedStatus === "all" ||
            user.status === selectedStatus;

        return (
            matchesSearch &&
            matchesRole &&
            matchesStatus
        );
    });

    userTableBody.innerHTML = "";

    filteredUsers.forEach((user) => {
        const isProvider = user.role === "provider";

        const bookingCount = isProvider
            ? user.rentalHistory.length
            : 0;

        const earnings = isProvider
            ? calculateProviderEarnings(user)
            : 0;

        const statusClass =
            user.status === "active"
                ? "status-verified"
                : "status-unpaid";

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>
                <div class="user-identity">
                    <div class="user-avatar">
                        ${escapeHTML(createInitials(user.name))}
                    </div>

                    <div>
                        <strong>${escapeHTML(user.name)}</strong>
                        <span>
                            ${escapeHTML(user.id)} ·
                            ${escapeHTML(user.email)}
                        </span>
                    </div>
                </div>
            </td>

            <td>
                <span class="role-badge role-${user.role}">
                    ${
                        user.role === "provider"
                            ? "Gear Provider"
                            : "Customer"
                    }
                </span>
            </td>

            <td>
                <span class="status-badge ${statusClass}">
                    ${escapeHTML(user.status)}
                </span>
            </td>

            <td class="amount-cell">
                ${isProvider ? user.gearListed : "—"}
            </td>

            <td class="amount-cell">
                ${isProvider ? bookingCount : "—"}
            </td>

            <td class="amount-cell">
                ${
                    isProvider
                        ? formatCurrency(earnings)
                        : "—"
                }
            </td>

            <td>${escapeHTML(user.joinedDate)}</td>

            <td>
                <div class="user-actions">
                    ${
                        isProvider
                            ? `
                                <button
                                    class="user-action-button details-button"
                                    data-action="details"
                                    data-user-id="${user.id}"
                                >
                                    History
                                </button>
                            `
                            : ""
                    }

                    <button
                        class="user-action-button"
                        data-action="edit"
                        data-user-id="${user.id}"
                    >
                        Edit
                    </button>

                    ${
                        user.status === "active"
                            ? `
                                <button
                                    class="user-action-button deactivate-button"
                                    data-action="deactivate"
                                    data-user-id="${user.id}"
                                >
                                    Deactivate
                                </button>
                            `
                            : `
                                <button
                                    class="user-action-button activate-button"
                                    data-action="activate"
                                    data-user-id="${user.id}"
                                >
                                    Activate
                                </button>
                            `
                    }
                </div>
            </td>
        `;

        userTableBody.appendChild(row);
    });

    userEmptyState.classList.toggle(
        "hidden",
        filteredUsers.length !== 0
    );
}

/* Add and edit user */

function openAddUserForm() {
    selectedUserId = null;

    userForm.reset();
    editingUserId.value = "";
    userFormTitle.textContent = "Add New User";
    userStatus.value = "active";

    userFormModal.classList.remove("hidden");
    document.body.style.overflow = "hidden";

    userName.focus();
}

function openEditUserForm(userId) {
    const user = users.find(
        (item) => item.id === userId
    );

    if (!user) {
        return;
    }

    selectedUserId = userId;

    editingUserId.value = user.id;
    userName.value = user.name;
    userEmail.value = user.email;
    userRole.value = user.role;
    userStatus.value = user.status;

    userFormTitle.textContent = "Edit User";

    userFormModal.classList.remove("hidden");
    document.body.style.overflow = "hidden";
}

function closeUserForm() {
    selectedUserId = null;
    userForm.reset();
    userFormModal.classList.add("hidden");
    document.body.style.overflow = "";
}

function saveUser(event) {
    event.preventDefault();

    const enteredName = userName.value.trim();
    const enteredEmail = userEmail.value.trim();
    const enteredRole = userRole.value;
    const enteredStatus = userStatus.value;

    const duplicateEmail = users.some(
        (user) =>
            user.email.toLowerCase() ===
                enteredEmail.toLowerCase() &&
            user.id !== editingUserId.value
    );

    if (duplicateEmail) {
        showToast("A user with this email already exists.");
        return;
    }

    if (editingUserId.value) {
        const existingUser = users.find(
            (user) => user.id === editingUserId.value
        );

        if (!existingUser) {
            return;
        }

        existingUser.name = enteredName;
        existingUser.email = enteredEmail;
        existingUser.status = enteredStatus;

        if (
            existingUser.role !== enteredRole &&
            enteredRole === "customer"
        ) {
            existingUser.gearListed = 0;
            existingUser.rentalHistory = [];
        }

        existingUser.role = enteredRole;

        showToast("User updated successfully.");
    } else {
        const userPrefix =
            enteredRole === "provider"
                ? "PRV"
                : "USR";

        users.unshift({
            id: `${userPrefix}-${Date.now()
                .toString()
                .slice(-6)}`,
            name: enteredName,
            email: enteredEmail,
            role: enteredRole,
            status: enteredStatus,
            joinedDate: "30 Aug 2026",
            gearListed: 0,
            rentalHistory: []
        });

        showToast("New user added successfully.");
    }

    closeUserForm();
    updateUserPage();
}

/* Provider details and history */

function openProviderDetails(userId) {
    const provider = users.find(
        (user) =>
            user.id === userId &&
            user.role === "provider"
    );

    if (!provider) {
        return;
    }

    document.getElementById(
        "providerAvatar"
    ).textContent = createInitials(provider.name);

    document.getElementById(
        "providerDetailsTitle"
    ).textContent = provider.name;

    document.getElementById(
        "providerDetailsEmail"
    ).textContent = `${provider.id} · ${provider.email}`;

    document.getElementById(
        "providerGearCount"
    ).textContent = provider.gearListed;

    document.getElementById(
        "providerBookingCount"
    ).textContent = provider.rentalHistory.length;

    document.getElementById(
        "providerEarnings"
    ).textContent = formatCurrency(
        calculateProviderEarnings(provider)
    );

    renderProviderHistory(provider);

    providerDetailsModal.classList.remove("hidden");
    document.body.style.overflow = "hidden";
}

function renderProviderHistory(provider) {
    const providerHistoryBody =
        document.getElementById("providerHistoryBody");

    const providerHistoryEmptyState =
        document.getElementById(
            "providerHistoryEmptyState"
        );

    providerHistoryBody.innerHTML = "";

    provider.rentalHistory.forEach((rental) => {
        const providerShare =
            rental.status === "refunded"
                ? 0
                : rental.amount * PROVIDER_RATE;

        const statusClass =
            rental.status === "completed"
                ? "status-verified"
                : rental.status === "active"
                    ? "status-pending"
                    : "status-refunded";

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>
                <strong>${escapeHTML(rental.bookingId)}</strong>
            </td>

            <td>${escapeHTML(rental.gearItem)}</td>

            <td>${escapeHTML(rental.customer)}</td>

            <td>${escapeHTML(rental.rentalPeriod)}</td>

            <td class="amount-cell">
                ${formatCurrency(rental.amount)}
            </td>

            <td class="commission-cell">
                ${formatCurrency(providerShare)}
            </td>

            <td>
                <span class="status-badge ${statusClass}">
                    ${escapeHTML(rental.status)}
                </span>
            </td>
        `;

        providerHistoryBody.appendChild(row);
    });

    providerHistoryEmptyState.classList.toggle(
        "hidden",
        provider.rentalHistory.length !== 0
    );
}

function closeProviderDetails() {
    providerDetailsModal.classList.add("hidden");
    document.body.style.overflow = "";
}

/* Deactivate and activate */

function openDeactivateModal(userId) {
    const user = users.find(
        (item) => item.id === userId
    );

    if (!user) {
        return;
    }

    selectedUserId = userId;
    deactivateUserName.textContent = user.name;

    deactivateModal.classList.remove("hidden");
    document.body.style.overflow = "hidden";
}

function closeDeactivateModal() {
    selectedUserId = null;
    deactivateModal.classList.add("hidden");
    document.body.style.overflow = "";
}

function confirmDeactivation() {
    const user = users.find(
        (item) => item.id === selectedUserId
    );

    if (!user) {
        return;
    }

    user.status = "deactivated";

    closeDeactivateModal();
    updateUserPage();
    showToast(`${user.name} has been deactivated.`);
}

function activateUser(userId) {
    const user = users.find(
        (item) => item.id === userId
    );

    if (!user) {
        return;
    }

    user.status = "active";

    updateUserPage();
    showToast(`${user.name} has been activated.`);
}

/* Table action handling */

userTableBody.addEventListener("click", (event) => {
    const actionButton = event.target.closest(
        "[data-action]"
    );

    if (!actionButton) {
        return;
    }

    const action = actionButton.dataset.action;
    const userId = actionButton.dataset.userId;

    if (action === "details") {
        openProviderDetails(userId);
    }

    if (action === "edit") {
        openEditUserForm(userId);
    }

    if (action === "deactivate") {
        openDeactivateModal(userId);
    }

    if (action === "activate") {
        activateUser(userId);
    }
});

/* Toast */

function showToast(message) {
    toast.textContent = message;
    toast.classList.remove("hidden");

    window.setTimeout(() => {
        toast.classList.add("hidden");
    }, 3000);
}

/* Update the complete page */

function updateUserPage() {
    updateUserSummary();
    renderUsers();
}

/* Search and filters */

userSearch.addEventListener("input", renderUsers);
roleFilter.addEventListener("change", renderUsers);
userStatusFilter.addEventListener(
    "change",
    renderUsers
);

/* User form events */

addUserButton.addEventListener(
    "click",
    openAddUserForm
);

userForm.addEventListener(
    "submit",
    saveUser
);

closeUserFormButton.addEventListener(
    "click",
    closeUserForm
);

cancelUserFormButton.addEventListener(
    "click",
    closeUserForm
);

/* Provider details events */

closeProviderDetailsButton.addEventListener(
    "click",
    closeProviderDetails
);

/* Deactivation events */

closeDeactivateButton.addEventListener(
    "click",
    closeDeactivateModal
);

cancelDeactivateButton.addEventListener(
    "click",
    closeDeactivateModal
);

confirmDeactivateButton.addEventListener(
    "click",
    confirmDeactivation
);

/* Provider sidebar filter */

providerNavigation.addEventListener("click", () => {
    roleFilter.value = "provider";
    userStatusFilter.value = "all";

    renderUsers();

    document
        .querySelector(".user-management-panel")
        .scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

    sidebar.classList.remove("open");
});

/* Mobile sidebar */

menuButton.addEventListener("click", () => {
    sidebar.classList.toggle("open");
});

/* Close modals by clicking outside */

[
    userFormModal,
    providerDetailsModal,
    deactivateModal
].forEach((modal) => {
    modal.addEventListener("click", (event) => {
        if (event.target !== modal) {
            return;
        }

        if (modal === userFormModal) {
            closeUserForm();
        }

        if (modal === providerDetailsModal) {
            closeProviderDetails();
        }

        if (modal === deactivateModal) {
            closeDeactivateModal();
        }
    });
});

/* Escape key closes open modals */

document.addEventListener("keydown", (event) => {
    if (event.key !== "Escape") {
        return;
    }

    if (!userFormModal.classList.contains("hidden")) {
        closeUserForm();
    }

    if (
        !providerDetailsModal.classList.contains("hidden")
    ) {
        closeProviderDetails();
    }

    if (!deactivateModal.classList.contains("hidden")) {
        closeDeactivateModal();
    }
});

/* Initial display */

updateUserPage();