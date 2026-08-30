"use strict";

const usersNavigationGroup =
    document.getElementById("usersNavigationGroup");

const usersMenuToggle =
    document.getElementById("usersMenuToggle");

function isUsersPage() {
    return window.location.pathname.endsWith(
        "/users.html"
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

function applyUserView() {
    if (!isUsersPage()) {
        return;
    }

    const requestedView =
        window.location.hash === "#providers"
            ? "providers"
            : "users";

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

    if (
        window.location.hash &&
        userPanel
    ) {
        userPanel.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
    }
}

if (usersMenuToggle) {
    usersMenuToggle.addEventListener(
        "click",
        (event) => {
            /*
             * Prevent app.js from treating the dropdown
             * toggle as a dashboard-section button.
             */
            event.stopImmediatePropagation();

            const isCurrentlyOpen =
                usersNavigationGroup.classList.contains(
                    "open"
                );

            setDropdownState(!isCurrentlyOpen);
        }
    );
}

if (isUsersPage()) {
    setDropdownState(true);
    applyUserView();
}

window.addEventListener(
    "hashchange",
    applyUserView
);