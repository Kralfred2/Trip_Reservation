// application/services/AuthService.js
export class AuthService {
  constructor(authAdapter, appState) {
    this.authAdapter = authAdapter;
    this.appState = appState;
  }

  // application/services/AuthService.js
async handleUnauthorizedAccess(requestedHash) {
  console.log("Checking authorization for:", requestedHash);

  const token = await this.authAdapter.getTokenFromLoc();

  if (token) {
    try {
      const user = await this.authAdapter.tryValidateToken(token.value);
      if (user) {
        this.appState.setUser(user);
        return; 
      }
    } catch (error) {
      console.error("Token validation failed");
    }
  }

  // FIX: Explicitly set state to GUEST so the Dispatcher stops ignoring requests
  this.appState.setUser(null); 

  console.log("No valid session found, redirecting to login.");
  this.appState.setRedirectUrl(requestedHash);
  window.location.hash = "/login";
}

  async login(email, username, password) {
    try {
      const results = await this.authAdapter.login(email, username, password);
      if (results.token && results.user) {
        this.appState.setUser(results.user);
        this.authAdapter.saveToken(results.token)


        const savedAddress = this.appState.getRedirectUrl();
        if (savedAddress) {
          console.log("Releasing user to:", savedAddress);
          this.appState.setRedirectUrl(null); // Clear it
          window.location.hash = savedAddress; 
        } else {
          window.location.hash = "/app"; 
        }
        
        return user;
      }
    } catch (error) {
      this.appState.setError(error.message);
      throw error;
    }
  }

    async register(email, username, password, role) {
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
      throw error;
    }
  }




  async logout() {
    this.authAdapter.tokenRepository.clearToken();
    this.appState.setUser(null);
    window.location.hash = "/login";
  }
}