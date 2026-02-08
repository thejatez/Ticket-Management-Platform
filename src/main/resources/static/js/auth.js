async function login() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    if (!username || !password) {
        alert("Please enter both username and password");
        return;
    }

    try {
        const res = await fetch(API + "/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (res.status === 401 || res.status === 400) {
            alert("Invalid username or password");
            document.getElementById("password").value = "";
            return;
        }

        if (!res.ok) {
            alert("Login failed. Please try again.");
            return;
        }

        const data = await res.json();

        if (!data.token || !data.role) {
            alert("Invalid server response. Please try again.");
            return;
        }

        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);

        if (data.role === "ADMIN") {
            window.location.href = "/admin-dashboard.html";
        } else {
            window.location.href = "/user-dashboard.html";
        }
    } catch (error) {
        console.error("Login error:", error);
        alert("Network error. Please try again.");
    }
}

async function register() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const roleValue = document.getElementById("role").value;

    await fetch(API + "/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            username,
            password,
            role: roleValue
        })
    });

    window.location.href = "/login.html";
}
