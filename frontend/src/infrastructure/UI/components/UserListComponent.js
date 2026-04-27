class UserListComponent {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.users = [];
    }

    // 1. Initial Fetch
    async init() {
        try {
            const response = await fetch('/api/users/accessible');
            if (!response.ok) throw new Error("Unauthorized");
            
            this.users = await response.json();
            this.render();
        } catch (error) {
            this.container.innerHTML = `<p class="error">Failed to load users: ${error.message}</p>`;
        }
    }

    // 2. Build the Table
    render() {
        if (this.users.length === 0) {
            this.container.innerHTML = "<p>No accessible users found.</p>";
            return;
        }

        const tableHTML = `
            <table class="user-table">
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${this.users.map(user => `
                        <tr>
                            <td>${user.username}</td>
                            <td>
                                ${user.hasMoreDetails ? 
                                    `<button class="details-btn" data-id="${user.id}">View Details</button>` : 
                                    `<span class="read-only-tag">Basic View</span>`
                                }
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        this.container.innerHTML = tableHTML;
        this.bindEvents();
    }

    // 3. Attach Click Listeners
    bindEvents() {
        const buttons = this.container.querySelectorAll('.details-btn');
        buttons.forEach(btn => {
            btn.addEventListener('click', (e) => {
                const userId = e.target.getAttribute('data-id');
                this.openModal(userId);
            });
        });
    }

    openModal(userId) {
        // This calls your existing UserSettingsModal logic
        console.log("Opening modal for User ID:", userId);
        const modal = new UserSettingsModal(userId); 
        modal.open();
    }
}

// To start it:
const list = new UserListComponent('user-list-container');
list.init();