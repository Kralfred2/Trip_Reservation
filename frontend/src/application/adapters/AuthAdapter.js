// application/adapters/AuthAdapter.js
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
      this.tokenRepository.clear();
      return null;
    }
  }

  async login(email, password) {
    const { user, token } = await this.userRepository.login(email, password);
    this.tokenRepository.saveToken(token);
    return user;
  }
}