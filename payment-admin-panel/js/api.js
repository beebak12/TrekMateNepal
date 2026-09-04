"use strict";

(function () {
    const API_BASE_URL = "http://127.0.0.1:5050/api";
    const TOKEN_KEY = "trekmate_admin_token";
    const USER_KEY = "trekmate_admin_user";

    function getToken() {
        return sessionStorage.getItem(TOKEN_KEY);
    }

    function getUser() {
        const value = sessionStorage.getItem(USER_KEY);

        if (!value) {
            return null;
        }

        try {
            return JSON.parse(value);
        } catch (_error) {
            sessionStorage.removeItem(USER_KEY);
            return null;
        }
    }

    function saveSession(token, user) {
        sessionStorage.setItem(TOKEN_KEY, token);
        sessionStorage.setItem(USER_KEY, JSON.stringify(user || {}));
    }

    function clearSession() {
        sessionStorage.removeItem(TOKEN_KEY);
        sessionStorage.removeItem(USER_KEY);
    }

    function goToLogin() {
        if (!window.location.pathname.endsWith("login.html")) {
            window.location.replace("login.html");
        }
    }

    async function request(path, options = {}) {
        const headers = new Headers(options.headers || {});
        const token = getToken();

        if (options.body && !headers.has("Content-Type")) {
            headers.set("Content-Type", "application/json");
        }

        if (token) {
            headers.set("Authorization", `Bearer ${token}`);
        }

        let response;

        try {
            response = await fetch(`${API_BASE_URL}${path}`, {
                ...options,
                headers
            });
        } catch (_error) {
            throw new Error(
                "Cannot connect to the TrekMate backend. Check that it is running on port 5050."
            );
        }

        const text = await response.text();
        let payload = {};

        if (text) {
            try {
                payload = JSON.parse(text);
            } catch (_error) {
                throw new Error("The backend returned an invalid response.");
            }
        }

        if (response.status === 401) {
            clearSession();
            goToLogin();
            throw new Error(payload.message || "Your admin session has expired.");
        }

        if (!response.ok || payload.success === false) {
            const validationMessage = Array.isArray(payload.errors)
                ? payload.errors.map((error) => error.msg).join(" ")
                : "";

            throw new Error(
                validationMessage || payload.message || "Request failed."
            );
        }

        return payload;
    }

    async function login(email, password) {
        const response = await request("/auth/login", {
            method: "POST",
            body: JSON.stringify({ email, password })
        });

        saveSession(response.token, response.user);

        try {
            await request("/admin/dashboard");
        } catch (error) {
            clearSession();

            if (error.message === "Access forbidden") {
                throw new Error("This account does not have administrator access.");
            }

            throw error;
        }

        return response.user;
    }

    function logout() {
        clearSession();
        window.location.replace("login.html");
    }

    window.TrekMateAPI = {
        baseUrl: API_BASE_URL,
        request,
        login,
        logout,
        getToken,
        getUser,
        requireAuth() {
            if (!getToken()) {
                goToLogin();
                return false;
            }

            return true;
        }
    };
})();
