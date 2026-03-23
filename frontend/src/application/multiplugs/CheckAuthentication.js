export class CheckAuthentication {
  constructor(AuthAdapter) {
    this.AuthAdapter = AuthAdapter;
  }

  async execute() {
    const token = this.AuthAdapter.getCurrentUser();

    if (!token) {
      return null;
    }

    try {
      const user = await this.userRepository.validateToken(token);
      return user;
    } catch {
      return null;
    }
  }
}