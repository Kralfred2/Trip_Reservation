// infrastructure/routing/Router.js
export class Router {
  constructor(appState, routes, container) {
    this.appState = appState;
    this.routes = routes;
    this.container = container; // The DOM element where views are injected

    // Listen for hash changes
    window.addEventListener("hashchange", () => this.handleRoute());
  }

  handleRoute() {
  const path = window.location.hash.slice(1) || "/login";
  const route = this.routes[path];

  if (route) {
    // Decision-making logic (Dimension of Logic)
    if (route.protected && !this.appState.isAuthenticated()) {
      window.location.hash = "/login";
      return;
    }

    // CREATE THE VIEW ONLY NOW (Lazy Loading)
    const viewInstance = route.createView(); 
    this.render(viewInstance);
  }
}

  render(view) {
    // Explicit DOM API operation: No innerHTML
    while (this.container.firstChild) {
      this.container.removeChild(this.container.firstChild);
    }
    
    const element = view.render(); 
    this.container.appendChild(element);
  }
}