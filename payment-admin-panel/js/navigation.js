"use strict";

const usersNavigationGroup =
    document.getElementById("usersNavigationGroup");

const usersMenuToggle =
    document.getElementById("usersMenuToggle");

function isUsersPage() {
    return /\/users(?:\.html)?\/?$/.test(
        window.location.pathname
    );
}

function setDropdownState(isOpen) {
    if (
        !usersNavigationGroup ||
        !usersMenuToggle
    ) {
        return;
    }

    usersNavigationGroup.classList.toggle(
        "open",
        isOpen
    );

    usersMenuToggle.setAttribute(
        "aria-expanded",
        String(isOpen)
    );
}

function clearMainNavigationSelection() {
    document.querySelectorAll(
        ".navigation-item"
    ).forEach((item) => {
        item.classList.remove("active");
    });
}

function activateMainNavigation(section) {
    clearMainNavigationSelection();

    const selectedNavigation =
        document.querySelector(
            `[data-section="${section}"]`
        );

    if (selectedNavigation) {
        selectedNavigation.classList.add(
            "active"
        );
    }
}

/*
 * Control Users and Providers views.
 */

function applyUserView() {
    if (!isUsersPage()) {
        return;
    }

    const requestedView =
        window.location.hash === "#providers"
            ? "providers"
            : "users";

    setDropdownState(true);

    clearMainNavigationSelection();

    if (usersMenuToggle) {
        usersMenuToggle.classList.add("active");
    }

    document.querySelectorAll(
        "[data-user-view]"
    ).forEach((item) => {
        item.classList.toggle(
            "active",
            item.dataset.userView === requestedView
        );
    });

    const roleFilter =
        document.getElementById("roleFilter");

    const statusFilter =
        document.getElementById(
            "userStatusFilter"
        );

    if (roleFilter) {
        roleFilter.value =
            requestedView === "providers"
                ? "provider"
                : "all";

        roleFilter.dispatchEvent(
            new Event("change")
        );
    }

    if (
        requestedView === "users" &&
        statusFilter
    ) {
        statusFilter.value = "all";

        statusFilter.dispatchEvent(
            new Event("change")
        );
    }

    const userPanel =
        document.querySelector(
            ".user-management-panel"
        );

    if (userPanel && window.location.hash) {
        requestAnimationFrame(() => {
            userPanel.scrollIntoView({
                behavior: "smooth",
                block: "start"
            });
        });
    }
}

/*
 * Handle links coming from users.html to sections
 * inside the Payment Dashboard.
 */

function applyDashboardRoute() {
    if (isUsersPage()) {
        return;
    }

    const requestedSection =
        window.location.hash.replace("#", "") ||
        "dashboard";

    const statusFilter =
        document.getElementById("statusFilter");

    if (requestedSection === "dashboard") {
        activateMainNavigation("dashboard");

        if (statusFilter) {
            statusFilter.value = "all";
            statusFilter.dispatchEvent(
                new Event("change")
            );
        }

        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });

        return;
    }

    if (requestedSection === "transactions") {
        activateMainNavigation("transactions");

        if (statusFilter) {
            statusFilter.value = "all";
            statusFilter.dispatchEvent(
                new Event("change")
            );
        }

        scrollToDashboardSection(
            "#transactions"
        );

        return;
    }

    if (requestedSection === "revenue") {
        activateMainNavigation("revenue");

        scrollToDashboardSection(
            "#revenue"
        );

        return;
    }

    if (requestedSection === "payouts") {
        activateMainNavigation("payouts");

        scrollToDashboardSection(
            "#payouts"
        );

        return;
    }

    if (requestedSection === "refunds") {
        activateMainNavigation("refunds");

        if (statusFilter) {
            statusFilter.value = "refunded";
            statusFilter.dispatchEvent(
                new Event("change")
            );
        }

        scrollToDashboardSection(
            "#transactions"
        );

        return;
    }

    activateMainNavigation("dashboard");
}

function scrollToDashboardSection(selector) {
    const section =
        document.querySelector(selector);

    if (!section) {
        return;
    }

    requestAnimationFrame(() => {
        section.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
    });
}

/*
 * Dropdown toggle.
 *
 * Capture mode and stopImmediatePropagation prevent
 * app.js from treating this as a dashboard button.
 */

if (usersMenuToggle) {
    usersMenuToggle.addEventListener(
        "click",
        (event) => {
            event.preventDefault();
            event.stopImmediatePropagation();

            if (!isUsersPage()) {
                window.location.href = "users.html";
                return;
            }

            const isCurrentlyOpen =
                usersNavigationGroup.classList.contains(
                    "open"
                );

            setDropdownState(!isCurrentlyOpen);
        },
        true
    );
}

/*
 * Initialise the correct page state.
 */

if (isUsersPage()) {
    applyUserView();
} else {
    applyDashboardRoute();
}

/*
 * Support changing views without a full reload.
 */

window.addEventListener(
    "hashchange",
    () => {
        if (isUsersPage()) {
            applyUserView();
        } else {
            applyDashboardRoute();
        }
    }
);
