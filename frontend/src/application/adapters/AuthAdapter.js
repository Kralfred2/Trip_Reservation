
export class AuthAdapter {
  constructor(userRepository, tokenRepository) {
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
  }     


  async getTokenFromLoc(){
    return this.tokenRepository.getToken(); 
  }

  async tryValidateToken(token){
   try {
    return await this.userRepository.validateToken(token); 
  } catch (error) {
    this.tokenRepository.clearToken(); 
    return null;
  }
  }

  async saveToken(token){
    this.tokenRepository.saveToken(token);
  }

async clearSession() {
    this.tokenRepository.clearToken();
  }

async login(email, username, password) {

  const response = await this.userRepository.login(email, username, password);
  console.log("Login user response" + JSON.stringify(response));
  console.log("Login token response" + response.token.token);
  return response;
  
}
}