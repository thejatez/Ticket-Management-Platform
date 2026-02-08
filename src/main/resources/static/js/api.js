const API = "http://localhost:8080";

function token() {
    return localStorage.getItem("token");
}

function role() {
    return localStorage.getItem("role");
}

function authHeaders() {
    return {
        "Authorization": "Bearer " + token(),
        "Content-Type": "application/json"
    };
}

async function validateToken() {
    if (!token()) {
        window.location.href = "/login.html";
        return false;
    }

    try {
        const res = await fetch(API + "/tickets/my-tickets", {
            method: "GET",
            headers: authHeaders()
        });

        if (res.status === 401 || res.status === 403) {
            localStorage.clear();
            window.location.href = "/login.html";
            return false;
        }

        return true;
    } catch (error) {
        console.error("Token validation failed", error);
        localStorage.clear();
        window.location.href = "/login.html";
        return false;
    }
}
