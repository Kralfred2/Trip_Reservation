// application/state/AppState.js

export const NavigationContext = {
  IDLE: 'IDLE',
  AUTHENTICATED: 'AUTHENTICATED',
  GUEST: 'GUEST',
  LOADING: 'LOADING'
};

export class App {
  constructor() {
    this.redirectUrl = null;
    this.state = {
      user: null, 
      context: NavigationContext.LOADING,
      error: null
    };
    this.listeners = [];
  }

  getUser() {
    return this.state.user; 
  }

  hasPermission(permission) {
    if (!this.state.user || !Array.isArray(this.state.user.permissions)) {
      return false;
    }
    return this.state.user.permissions.includes(permission);
  }

  isAuthenticated() {
    return this.state.context === NavigationContext.AUTHENTICATED; //
  }

  getContext() {
    return this.state.context;
  }

  setUser(user) {
    
    if (user) {
      this.state = {
        ...this.state,
        user: user, //
        context: NavigationContext.AUTHENTICATED
      };
      console.log("User set to: " + this.state);
    } else {
      this.state = {
        ...this.state,
        user: null, //
        context: NavigationContext.GUEST
      };
    }
    this.notify();
  }

  setError(error) {
  if (this.state.error === error) return; 

  this.state.error = error;
  this.notify();
}

  subscribe(callback) {
    this.listeners.push(callback);
  }

  notify() {
    this.listeners.forEach((callback) => callback(this.state));
  }
  setRedirectUrl(url) { this.redirectUrl = url; }
  getRedirectUrl() { return this.redirectUrl; }
}