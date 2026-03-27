
export class Router {
  constructor(appState, routes, container) {
    this.appState = appState;
    this.routes = routes;
    this.container = container;


    window.addEventListener("hashchange", () => this.handleRoute());
  }

  handleRoute() {
  const path = window.location.hash.slice(1) || "/login";
  const route = this.routes[path];

  if (route) {
    if (route.protected && !this.appState.isAuthenticated()) {
      window.location.hash = "/login";
      return;
    }

    const viewInstance = route.createView(); 
    this.render(viewInstance);
  }
}

  render(view) {
    while (this.container.firstChild) {
      this.container.removeChild(this.container.firstChild);
    }
    
    const element = view.render(); 
    this.container.appendChild(element);
  }
}