// infrastructure/UI/components/UserComponent.js
import { UserSettingsModal } from './UserSettingsModal.js';

export class UserComponent {
    constructor(userData, userSettingsModal) {
        this.userData = userData;
        this.userSettingsModal = userSettingsModal;
    }

    render() {
        const card = document.createElement("div");
        card.className = "user-card";
        
        card.innerHTML = `
            <div class="user-info">
                <h3>${this.userData.username}</h3>
                <p>${this.userData.id}</p>
            </div>
            <button class="edit-settings-btn">Edit Settings</button>
        `;

        if(this.userData.hasMoreDetails){
        const editBtn = card.querySelector(".edit-settings-btn");


        editBtn.onclick = () => {
    // 1. Create instance
    console.log("clicked");
    const modal = new UserSettingsModal(this.userData, async (id, data) => {
        await this.userRepository.updateUser(id, data);
    });

    // 2. Call render() to get the HTML Element
    const modalElement = modal.render();

    // 3. CRITICAL: Append it to the live DOM
    document.body.appendChild(modalElement);
};
        }

        return card;
    }
}