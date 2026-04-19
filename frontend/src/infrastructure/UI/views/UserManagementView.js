// infrastructure/UI/views/UserManagementView.js
import { UserComponent } from '../components/UserComponent.js';

export class UserManagementView {
  constructor(appState, userRepository) {
    this.appState = appState;
    this.userRepository = userRepository;
  }

  render() {
    const container = document.createElement("div");
    container.innerHTML = "<h1>User Management</h1><div id='user-list'>Loading users...</div>";

    this.loadUsers(container);

    return container;
  }

  async loadUsers(container) {
    try {
      const users = await this.userRepository.findAll(); 
      const listElement = container.querySelector("#user-list");
      listElement.innerHTML = ""; 

      users.forEach(userData => {
        // Create the component for each user
        const userComp = new UserComponent(userData, this.appState);
        listElement.appendChild(userComp.render());
      });
    } catch (error) {
      container.innerHTML = `<p style="color:red">Error loading users: ${error.message}</p>`;
    }
  }
}