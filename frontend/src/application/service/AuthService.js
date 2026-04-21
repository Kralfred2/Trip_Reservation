// application/services/AuthService.js
// application/services/AuthService.js
export class AuthService {
    constructor(userRepository, tokenRepository, appState) {
        this.userRepository = userRepository;    // The API Repo
        this.tokenRepository = tokenRepository;  // The LocalStorage/Cookie Repo
        this.appState = appState;                // The UI State
    }


async applyAuthentication(userObject, tokenObject) {
        // 1. Update UI State
        this.appState.setUser(userObject);

        if (tokenObject) {
            // Extract string for the Network Layer (BaseApiRepository)
            const tokenString = typeof tokenObject === 'object' ? tokenObject.value : tokenObject;
            
            // 2. Update Network Layer
            this.userRepository.setToken(tokenString);
            
            // 3. Update Persistence Layer
            this.tokenRepository.saveToken(tokenObject);
        } else {
            this.tokenRepository.clearToken();
            this.userRepository.setToken(null);
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
    const tokenObject = this.tokenRepository.getToken(); 
    
console.warn("Token " + JSON.stringify(tokenObject));
    if (tokenObject && tokenObject !== "undefined") { 
        try {
             
            const response = await this.userRepository.validateToken(tokenObject.value);
            if (response) {
                this.applyAuthentication(response.user, response.token);
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