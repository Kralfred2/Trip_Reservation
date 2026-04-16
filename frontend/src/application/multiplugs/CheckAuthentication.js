export class CheckAuthentication {
  constructor(authAdapter, appState) {
    this.authAdapter = authAdapter; 
    this.appState = appState;       
  }

  async execute() {
    try {
      const user = await this.authAdapter.getCurrentUser();
      
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