// application/state/AppState.js

export const NavigationContext = {
  IDLE: 'IDLE',
  AUTHENTICATED: 'AUTHENTICATED',
  GUEST: 'GUEST',
  LOADING: 'LOADING'
};

export class App {
  constructor() {
    this.state = {
      user: null, //
      context: NavigationContext.LOADING,
      error: null
    };
    this.listeners = [];
  }

  // --- Selectors (Explicit Decision-Making Dimensions) ---

  getUser() {
    return this.state.user; //
  }

  isAuthenticated() {
    return this.state.context === NavigationContext.AUTHENTICATED; //
  }

  getContext() {
    return this.state.context;
  }

  // --- Dispatcher (State Transitions) ---

  setUser(user) {
    
    if (user) {
      this.state = {
        ...this.state,
        user: user, //
        context: NavigationContext.AUTHENTICATED
      };
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
    this.state = { ...this.state, error };
    this.notify();
  }

  // --- Observer Pattern Logic ---

  subscribe(callback) {
    this.listeners.push(callback);
  }

  notify() {
    this.listeners.forEach((callback) => callback(this.state));
  }
}