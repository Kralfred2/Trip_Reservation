export class App {
  constructor() {
    this.user = null;
  }

  setUser(user) {
    this.user = user;
  }

  getUser() {
    return this.user;
  }

  isAuthenticated() {
    return this.user !== null;
  }
}