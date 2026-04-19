// application/services/AuthService.js
// application/services/AuthService.js
export class AuthService {
    constructor(userRepository, tokenRepository, appState) {
        this.userRepository = userRepository;    // The API Repo
        this.tokenRepository = tokenRepository;  // The LocalStorage/Cookie Repo
        this.appState = appState;                // The UI State
    }


    async applyAuthentication(user, tokenValue) {

       
  
        console.warn("updating token 1  " + tokenValue);
    
       console.warn("updating token 1  " + JSON.stringify(user));
      this.appState.setUser(user)

        // 3. Update Storage (Persistence)
        if (tokenValue) {
          console.warn("saving token  22 " + JSON.stringify(tokenValue));
          this.userRepository.setToken(tokenValue);
          this.tokenRepository.saveToken(tokenValue);
        } else {
            this.tokenRepository.clearToken();
        }
    }

    async login(email, username, password) {
    try {

        const result = await this.userRepository.login(email, username, password);

        await this.applyAuthentication(result.user, result.token);
        
        window.location.hash = "/home";
    } catch (error) {
        console.error("Login Error:", error.message);
        throw error;
    }
}


async handleUnauthorizedAccess() {
    const tokenValue = this.tokenRepository.getToken(); 
    
console.warn("Token " + JSON.stringify(tokenValue));
    // ONLY try to validate if the token string actually exists
    if (tokenValue && tokenValue !== "undefined") { 
        try {
             
            const user = await this.userRepository.validateToken(tokenValue);
            if (user) {
                this.applyAuthentication(user, tokenValue);
                return;
            }
        } catch (e) {
            console.warn("Token expired or invalid", e);
        }
    }

    this.applyAuthentication(null, null);
    if (window.location.hash !== "#/login") {
        window.location.hash = "/login";
    }
}

    logout() {
        this.applyAuthentication(null, null);
        window.location.hash = "/login";
    }
}