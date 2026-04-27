export class UserSettingsModal {
    static loadStyles() {
        if (document.getElementById('user-modal-css')) return;

        const link = document.createElement('link');
        link.id = 'user-modal-css';
        link.rel = 'stylesheet';
        // Adjust this path relative to your index.html
        link.href = 'src/infrastructure/UI/styles/UserSettingsModal.css'; 
        document.head.appendChild(link);
    }

    constructor(user, onSave) {
        UserSettingsModal.loadStyles();
        this.user = user;
        this.onSave = onSave;
    }

    render() {
        const overlay = document.createElement("div");
        overlay.className = "modal-overlay";
        
        overlay.innerHTML = `
            <div class="modal-content">
                <h2>User Settings</h2>
                <form id="edit-user-form">
                    <label>Username</label>
                    <input type="username" name="username" value="${this.user.username}" required>

                    <label>Email</label>
                    <input type="email" name="email" value="${this.user.email}" required>
                    
                    <label>Role</label>
                    <select name="role">
                        <option value="ROLE_USER" ${this.user.role === 'ROLE_USER' ? 'selected' : ''}>User</option>
                        <option value="ROLE_ADMIN" ${this.user.role === 'ROLE_ADMIN' ? 'selected' : ''}>Admin</option>
                    </select>

                    <div class="modal-actions">
                        <button type="submit" class="save-btn">Save</button>
                        <button type="button" class="cancel-btn">Cancel</button>
                    </div>
                </form>
            </div>
        `;

        overlay.querySelector(".cancel-btn").onclick = () => overlay.remove();
        overlay.querySelector("#edit-user-form").onsubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            await this.onSave(this.user.id, {
                username: formData.get("username"),
                email: formData.get("email"),
                role: formData.get("role")
            });
            overlay.remove();
        };

        return overlay;
    }
}