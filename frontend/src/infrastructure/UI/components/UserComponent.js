// infrastructure/UI/components/UserComponent.js
export class UserComponent {
  constructor(user, appState) {
    this.user = user; 
    this.appState = appState;
  }

  render() {
    const card = document.createElement("div");
    card.className = "user-card";
    card.style = "border: 1px solid #ccc; padding: 10px; margin: 5px; display: flex; justify-content: space-between;";

    const info = document.createElement("div");
    info.innerHTML = `<strong>${this.user.username}</strong> (${this.user.email})`;
    card.appendChild(info);

    const actions = document.createElement("div");

    if (this.appState.hasPermission("can_modify_users")) {
      const editBtn = document.createElement("button");
      editBtn.innerText = "Modify";
      editBtn.onclick = () => console.log(`Editing user ${this.user.id}`);
      actions.appendChild(editBtn);
    }

    if (this.appState.hasPermission("can_delete_users")) {
      const deleteBtn = document.createElement("button");
      deleteBtn.innerText = "Delete";
      deleteBtn.style.color = "red";
      deleteBtn.onclick = () => console.log(`Deleting user ${this.user.id}`);
      actions.appendChild(deleteBtn);
    }

    card.appendChild(actions);
    return card;
  }
}