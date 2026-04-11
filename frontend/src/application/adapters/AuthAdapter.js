
export class AuthAdapter {
  constructor(userRepository, tokenRepository) {
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
  }


async getCurrentUser() {
  const tokenEntity = this.tokenRepository.getToken(); 

  if (!tokenEntity || !tokenEntity.value) return null;

  try {
    return await this.userRepository.validateToken(tokenEntity.value); 
  } catch (error) {
    this.tokenRepository.clearToken(); // Clear bad cookies
    return null;
  }
}

async login(email, username, password) {

  const result = await this.userRepository.login(email, username, password);
  
  if (result && result.token) {
    this.tokenRepository.saveToken(result.token); 
    return result.user;
  }
  
  throw new Error("Login failed: No token returned");
}
}