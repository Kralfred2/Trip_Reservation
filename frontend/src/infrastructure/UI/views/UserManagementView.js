// infrastructure/UI/views/UserManagementView.js
import { UserComponent } from '../components/UserComponent.js';
import { UserSettingsModal } from '../components/UserSettingsModal.js';

export class UserManagementView {
  constructor(appState, userRepository) {
    this.appState = appState;
    this.userRepository = userRepository;

  }

  render() {
    const container = document.createElement("div");
    if(this.userRepository == null){
          console.error("View Error:");
    }
    container.innerHTML = "<h1>User Management</h1><div id='user-list'>Loading users...</div>";

     this.loadUsers(container);

    return container;
  }

async loadUsers(container) {
    try {
      const data = await this.userRepository.getAllUsers();
      
      // If the API returns { users: [...] } instead of [...], extract the array
      const users = Array.isArray(data) ? data : (data.users || []);

      const listElement = container.querySelector("#user-list");
      listElement.innerHTML = ""; 

      users.forEach(userData => {
        // Inside UserManagementView.js loop:
        const userComp = new UserComponent(userData, new UserSettingsModal());
        listElement.appendChild(userComp.render());
      });
    } catch (error) {
      console.error("View Error:", error);
      container.innerHTML = `<p style="color:red">Error loading users: ${error.message}</p>`;
    }
}
}