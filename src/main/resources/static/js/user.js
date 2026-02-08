async function loadMyTickets() {

    const res = await fetch(API + "/tickets/my", {
        headers: authHeaders()
    });

    const tickets = await res.json();

    const table = document.getElementById("tickets");
    table.innerHTML = "";

    for (let t of tickets) {
        let actionBtn = '';
        if (t.status === 'OPEN') {
            actionBtn = `<button onclick="closeTicket(${t.id})" style="background-color: #ff4444; color: white; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">Close</button>`;
            actionBtn += `<button onclick="viewTicketHistory(${t.id})" style="background-color: #4444ff; color: white; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">History</button>`;
            actionBtn += `<button onclick="deleteTicket(${t.id})" style="background-color: #8b0000; color: white; padding: 5px 10px; border: none; cursor: pointer;">Delete</button>`;
        } else if (t.status === 'CLOSED') {
            actionBtn = `<button onclick="reopenTicket(${t.id})" style="background-color: #44ff44; color: black; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">Reopen</button>`;
            actionBtn += `<button onclick="viewTicketHistory(${t.id})" style="background-color: #4444ff; color: white; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">History</button>`;
            actionBtn += `<button onclick="deleteTicket(${t.id})" style="background-color: #8b0000; color: white; padding: 5px 10px; border: none; cursor: pointer;">Delete</button>`;
        }
        
        table.innerHTML += `
        <tr>
            <td>${t.id}</td>
            <td>${t.title}</td>
            <td>${t.description}</td>
            <td>${t.status}</td>
            <td>${actionBtn}</td>
        </tr>`;
    }
}

async function createTicket() {

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;

    if (!title || !description) {
        alert("Please enter both title and description");
        return;
    }

    try {
        const res = await fetch(API + "/tickets", {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify({ title, description })
        });

        if (!res.ok) {
            alert("Failed to create ticket. Please try again.");
            return;
        }

        alert("Ticket created successfully!");
        document.getElementById("title").value = "";
        document.getElementById("description").value = "";
        loadMyTickets();
    } catch (error) {
        console.error("Create ticket error:", error);
        alert("Error creating ticket. Please try again.");
    }
}

async function closeTicket(id) {
    try {
        const res = await fetch(API + `/tickets/${id}/close`, {
            method: "PUT",
            headers: authHeaders()
        });

        if (!res.ok) {
            alert("Failed to close ticket");
            return;
        }

        alert("Ticket closed successfully!");
        loadMyTickets();
    } catch (error) {
        console.error("Close ticket error:", error);
        alert("Error closing ticket");
    }
}

async function reopenTicket(id) {
    try {
        const res = await fetch(API + `/tickets/${id}/reopen`, {
            method: "PUT",
            headers: authHeaders()
        });

        if (!res.ok) {
            alert("Failed to reopen ticket");
            return;
        }

        alert("Ticket reopened successfully!");
        loadMyTickets();
    } catch (error) {
        console.error("Reopen ticket error:", error);
        alert("Error reopening ticket");
    }
}

async function deleteTicket(id) {
    if (!confirm('Are you sure you want to delete this ticket?')) {
        return;
    }
    
    try {
        const res = await fetch(API + `/tickets/${id}`, {
            method: "DELETE",
            headers: authHeaders()
        });

        if (!res.ok) {
            alert("Failed to delete ticket");
            return;
        }

        alert("Ticket deleted successfully!");
        loadMyTickets();
    } catch (error) {
        console.error("Delete ticket error:", error);
        alert("Error deleting ticket");
    }
}
function viewTicketHistory(id) {
    window.location.href = `/ticket-history.html?id=${id}`;
}