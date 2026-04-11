// application/services/AuthService.js
export class AuthService {
  constructor(authAdapter, appState) {
    this.authAdapter = authAdapter;
    this.appState = appState;
  }

  async login(email, username, password) {
    try {
      const user = await this.authAdapter.login(email, username, password);
      if (user) {
        this.appState.setUser(user);
        console.log("Login succ:");
        return user;
      }
    } catch (error) {
      console.log("Login not succ:");
      this.appState.setError(error.message);
      console.log("Login not succ:");
      throw error;
    }
  }


  async checkToken() {
    try {

      const user = await this.authAdapter.getCurrentUser();
      console.log("CheckToken result:", user);
      if (user) {
        this.appState.setUser(user);
        console.log("AppState Context after update:", this.appState.getContext());
        return user;
      }
      this.appState.setUser(null);
      return null;
    } catch (error) {
        console.log("Not Nice job: ");
      this.appState.setUser(null);
      return null;
    }
  }

  async logout() {
    this.authAdapter.tokenRepository.clearToken();
    this.appState.setUser(null);
    window.location.hash = "/login";
  }
}