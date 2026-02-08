async function loadAllTickets() {

    const res = await fetch(API + "/admin/tickets", {
        headers: authHeaders()
    });

    const tickets = await res.json();

    const table = document.getElementById("tickets");
    table.innerHTML = "";

    for (let t of tickets) {
        let actionBtn = '';
        if (t.status === 'OPEN') {
            actionBtn = `<button onclick="adminCloseTicket(${t.id})" style="background-color: #ff4444; color: white; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">Close</button>`;
            actionBtn += `<button onclick="viewAdminTicketHistory(${t.id})" style="background-color: #4444ff; color: white; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">History</button>`;
            actionBtn += `<button onclick="adminDeleteTicket(${t.id})" style="background-color: #8b0000; color: white; padding: 5px 10px; border: none; cursor: pointer;">Delete</button>`;
        } else if (t.status === 'CLOSED') {
            actionBtn = `<button onclick="adminReopenTicket(${t.id})" style="background-color: #44ff44; color: black; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">Reopen</button>`;
            actionBtn += `<button onclick="viewAdminTicketHistory(${t.id})" style="background-color: #4444ff; color: white; padding: 5px 10px; border: none; cursor: pointer; margin-right: 5px;">History</button>`;
            actionBtn += `<button onclick="adminDeleteTicket(${t.id})" style="background-color: #8b0000; color: white; padding: 5px 10px; border: none; cursor: pointer;">Delete</button>`;
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

async function adminCloseTicket(id) {
    await fetch(API + `/tickets/${id}/close`, {
        method: "PUT",
        headers: authHeaders()
    });
    loadAllTickets();
}

async function adminReopenTicket(id) {
    await fetch(API + `/tickets/${id}/reopen`, {
        method: "PUT",
        headers: authHeaders()
    });
    loadAllTickets();
}

async function adminDeleteTicket(id) {
    if (!confirm('Are you sure you want to delete this ticket?')) {
        return;
    }
    
    await fetch(API + `/admin/tickets/${id}`, {
        method: "DELETE",
        headers: authHeaders()
    });
    loadAllTickets();
}

function viewAdminTicketHistory(id) {
    window.location.href = `/ticket-history.html?id=${id}`;
}
