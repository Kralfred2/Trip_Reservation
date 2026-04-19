// infrastructure/ui/components/Navbar.js
// infrastructure/ui/components/Navbar.js
export class Navbar {
  constructor(authService, appState, navigationDispatcher) {
    this.authService = authService;
    this.appState = appState;
    this.navigationDispatcher = navigationDispatcher;
  }

  render() {
    const nav = document.createElement("nav");
    nav.className = "navbar"; 
    Object.assign(nav.style, {
      display: "flex",
      justifyContent: "space-between", 
      alignItems: "center",
      padding: "10px 20px",
      backgroundColor: "#2c3e50",
      color: "white"
    });

    const user = this.appState.getUser();

    const brand = document.createElement("div");
    brand.textContent = "🏨 ReservationApp";
    brand.style.fontWeight = "bold";
    nav.appendChild(brand);

    const actions = document.createElement("div");
    actions.style.display = "flex";
    actions.style.alignItems = "center";

    if (user) {
      if (user.role === 'ROLE_ADMIN' || user.permissions.includes('view_users')) {
        const adminLink = document.createElement("a");
        adminLink.textContent = "Admin Panel";
        adminLink.href = "#";
        adminLink.style.marginRight = "20px";
        adminLink.style.color = "#ecf0f1";
        adminLink.onclick = (e) => {
          e.preventDefault();
          this.navigationDispatcher.dispatch("/admin/view/users");
        };
        actions.appendChild(adminLink);
      }

      const userInfo = document.createElement("span");

      userInfo.innerHTML = `
        <small style="display:block; font-size: 0.7em; color: #bdc3c7;">${user.role}</small>
        ${user.username}
      `;
      userInfo.style.marginRight = "15px";

      const logoutBtn = document.createElement("button");
      logoutBtn.textContent = "Logout";
      logoutBtn.style.padding = "5px 10px";
      logoutBtn.onclick = () => this.handleLogout();

      actions.append(userInfo, logoutBtn);
    }

    nav.appendChild(actions);
    return nav;
  }

  async handleLogout() {
    await this.authService.logout();
  }
}