// application/navigation/Dispatcher.js
// application/navigation/NavigationDispatcher.js
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

  handleStateChange() {
    const isAuth = this.appState.isAuthenticated();
    const currentPath = window.location.hash.replace("#", "") || "/home";

    // Security & UX logic: Redirect based on new Auth status
    if (isAuth && (currentPath === "/login" || currentPath === "/register")) {
      window.location.hash = "/app";
    } else if (!isAuth && currentPath === "/app") {
      window.location.hash = "/login";
    } else {
      this.reDispatch();
    }
  }

  reDispatch() {
    if (this.router) {
      this.router.handleRoute();
    }
  }

  async dispatch(route) {
    const context = this.appState.getContext();
    if (context === 'LOADING') return;

    const isAuth = this.appState.isAuthenticated();

    if (route.protected && !isAuth) {
      this.appState.setRedirectUrl(window.location.hash);
      window.location.hash = "/login";
      return;
    }

    if (isAuth && (route.path === '/login' || route.path === '/register')) {
      window.location.hash = "/app"; 
      return;
    }

    this.appState.setError(null);


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
      const navbar = new Navbar(this.authService, this.appState);
      this.container.appendChild(navbar.render());
    }

    // Add the main view content
    const pageElement = view.render();
    this.container.appendChild(pageElement);
  }
}