import { StyleLoader } from '../utils/StyleLoader.js'; // Adjust path as needed

export class UserSettingsModal {
    constructor(user, currentPerms, onSave, fieldGenerator, config) {
        // Load the specific CSS for this modal
        StyleLoader.load('src/infrastructure/UI/styles/UserSettingsModal.css');
        
        this.user = user;
        this.currentPerms = currentPerms;
        this.onSave = onSave;
        this.fieldGenerator = fieldGenerator;
        this.config = config;
    }

    render() {
        const overlay = document.createElement("div");
        overlay.className = "modal-overlay";
        
        // Use the Field Generator and Config to create the HTML
        const fieldsHtml = Object.keys(this.config).map(key => {
            const config = this.config[key];
            const value = this.currentPerms[key];
            return this.fieldGenerator(key, config, value);
        }).join('');

        overlay.innerHTML = `
            <div class="modal-content">
                <h2>Edit Permissions: ${this.user.username}</h2>
                <form id="modal-form">
                    <div class="permissions-grid">
                        ${fieldsHtml}
                    </div>
                    <div class="modal-actions">
                        <button type="submit" class="save-btn">Save Changes</button>
                        <button type="button" class="cancel-btn">Cancel</button>
                    </div>
                </form>
            </div>
        `;

        overlay.querySelector(".cancel-btn").onclick = () => overlay.remove();
        overlay.querySelector("#modal-form").onsubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const dataToSave = {};

            // Map form data back to JSON keys
            Object.keys(this.config).forEach(key => {
                const type = this.config[key].type;
                if (type === 'CHECKBOX') {
                    dataToSave[key] = formData.get(`perm_${key}`) === 'on';
                } else {
                    dataToSave[key] = formData.get(`perm_${key}`);
                }
            });

            await this.onSave(this.user.id, dataToSave);
            overlay.remove();
        };

        return overlay;
    }
}