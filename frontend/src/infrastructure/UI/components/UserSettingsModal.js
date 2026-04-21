// infrastructure/UI/components/UserSettingsModal.js
export class UserSettingsModal {
    constructor(user, onSave) {
        this.user = user;
        this.onSave = onSave; // Callback function for when saving is successful
    }

    render() {
        const overlay = document.createElement("div");
        overlay.className = "modal-overlay";
        
        overlay.innerHTML = `
            <div class="modal-content">
                <h2>Edit User: ${this.user.username}</h2>
                <form id="edit-user-form">
                    <label>Email:</label>
                    <input type="email" name="email" value="${this.user.email}" required>
                    
                    <label>Role:</label>
                    <select name="role">
                        <option value="ROLE_USER" ${this.user.role === 'ROLE_USER' ? 'selected' : ''}>User</option>
                        <option value="ROLE_ADMIN" ${this.user.role === 'ROLE_ADMIN' ? 'selected' : ''}>Admin</option>
                    </select>

                    <div class="modal-actions">
                        <button type="submit" class="save-btn">Save Changes</button>
                        <button type="button" class="cancel-btn">Cancel</button>
                    </div>
                </form>
            </div>
        `;

        // Close on cancel
        overlay.querySelector(".cancel-btn").onclick = () => overlay.remove();

        // Handle Form Submit
        overlay.querySelector("#edit-user-form").onsubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const updatedData = {
                email: formData.get("email"),
                role: formData.get("role")
            };
            
            await this.onSave(this.user.id, updatedData);
            overlay.remove();
        };

        return overlay;
    }
}