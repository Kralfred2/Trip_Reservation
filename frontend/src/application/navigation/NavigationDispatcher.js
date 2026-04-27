
import { Navbar } from "../../infrastructure/UI/components/Navbar.js";

export class NavigationDispatcher {
  constructor(appState, viewFactory, container, authService) {
    this.appState = appState;
    this.viewFactory = viewFactory;
    this.container = container;
    this.authService = authService;
    this.router = null; 
    
    this.appState.subscribe(() => {
      this.handleStateChange();
    });
  }

  setRouter(router) {
    this.router = router;
  }

  // NavigationDispatcher.js
handleStateChange() {
    const isAuth = this.appState.isAuthenticated();
    const currentHash = window.location.hash || "#/home";
    const currentPath = currentHash.replace("#", "");

    // Guard: If we are already on the login page and NOT authenticated, stop.
    if (!isAuth && currentPath === "/login") {
        return; 
    }

          console.log("Current path:  " + currentPath);
      console.log("Current hash:  " + currentHash);

    if (isAuth && (currentPath === "/login" || currentPath === "/register")) {

        window.location.hash = "/app";
    } else if (!isAuth) {
      this.appState.setRedirectUrl(currentPath);
        window.location.hash = "/login";
    }  
    else {
        this.reDispatch();
    }
}

  reDispatch() {
    if (this.router) {
      this.router.handleRoute();
    }
  }

  async dispatch(routeOrPath) {
    const context = this.appState.getContext();
    if (context === 'LOADING') return;

    // 1. Convert string to route object if necessary
    let route = routeOrPath;
    if (typeof routeOrPath === 'string') {
      // Ask the router to find the object { path, protected, createView }
      route = this.router.findRoute(routeOrPath);
    }

    if (!route) {
      console.error("No route found for:", routeOrPath);
      return;
    }

    const isAuth = this.appState.isAuthenticated();

    // 2. Now 'route' is an object, so these checks work
    if (route.protected && !isAuth) {
      this.authService.handleUnauthorizedAccess(window.location.hash);
      return;
    }

    // 3. This now works because route is the object from routes.js
    const view = route.createView(); 
    this.render(view);
  }

  render(view) {
    if (!view || typeof view.render !== 'function') {
      console.error("Dispatcher Error: View is invalid or missing a render method", view);
      return;
    }

    this.container.innerHTML = ""; 


    if (this.appState.isAuthenticated()) {
      const navbar = new Navbar(this.authService, this.appState, this);
      this.container.appendChild(navbar.render());
    }


    const pageElement = view.render();
    this.container.appendChild(pageElement);
  }
}