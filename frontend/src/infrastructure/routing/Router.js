
export class Router {
constructor(routes, dispatcher) {
  this.dispatcher = dispatcher;
  this.routes = routes;
  this.handleRoute = this.handleRoute.bind(this);
}

findRoute(path) {
    // We clean the path to ensure matches (removing # or trailing slashes)
    const cleanPath = path.replace('#', '') || '/';
    return this.routes.find(r => r.path === cleanPath);
  }


handleRoute() {
  const hash = window.location.hash || "#/home";
  const path = hash.replace("#", ""); 


  if (!Array.isArray(this.routes)) {
    console.error("Router error: this.routes is not an array!");
    return;
  }

  let route = this.routes.find(r => r.path === path);


  if (!route) {
    console.warn(`Route "${path}" not found. Redirecting to /home.`);
    window.location.hash = "/home"; 
    return;
  }
  

  this.dispatcher.dispatch(route);
}
}