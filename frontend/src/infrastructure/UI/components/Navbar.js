// infrastructure/ui/components/Navbar.js
export class Navbar {
  constructor(authService, appState) {
    this.authService = authService;
    this.appState = appState;
  }

  render() {
    const nav = document.createElement("nav");
    nav.style.display = "flex";
    nav.style.justifyContent = "flex-end";
    nav.style.padding = "10px";
    nav.style.backgroundColor = "#f4f4f4";

    const user = this.appState.getUser();
    if (user) {
      const userInfo = document.createElement("span");
      userInfo.textContent = `Hello, ${user.username} `;
      userInfo.style.marginRight = "15px";

      const logoutBtn = document.createElement("button");
      logoutBtn.textContent = "Logout";
      logoutBtn.onclick = () => this.handleLogout();

      nav.append(userInfo, logoutBtn);
    }

    return nav;
  }

  async handleLogout() {
    await this.authService.logout();
  }
}