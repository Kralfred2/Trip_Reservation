export class CheckAuthentication {
  constructor(authAdapter, appState) {
    this.authAdapter = authAdapter; // The "How"
    this.appState = appState;       // The "Where" to store it
  }

  async execute() {
    try {
      // 1. Ask the adapter to verify the current token
      const user = await this.authAdapter.getCurrentUser();

      // 2. Centrally update the state
      if (user) {
        this.appState.setUser(user); 
        return user;
      }
      
      this.appState.setUser(null);
      return null;
    } catch (error) {
      this.appState.setUser(null);
      return null;
    }
  }
}