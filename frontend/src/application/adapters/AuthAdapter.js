
export class AuthAdapter {
  constructor(userRepository, tokenRepository) {
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
  }

  async getCurrentUser() {
    const token = this.tokenRepository.getToken();

    if (!token) return null;

    try {
      return await this.userRepository.validateToken(token);
    } catch {
      this.tokenRepository.clearToken();
      return null;
    }
  }

  // application/adapters/AuthAdapter.js
async login(email, username, password) {
  // 1. Get the data from Java
  const result = await this.userRepository.login(email, username, password);
  
  // 2. 'result.token' is the Token OBJECT {value, expiresAt}
  if (result && result.token) {
    this.tokenRepository.saveToken(result.token); 
    return result.user;
  }
  
  throw new Error("Login failed: No token returned");
}
}