export class UserRepository {
  async validateToken(token) {
    throw new Error("Not implemented")
  }
  async login(email,username, password){
    throw new Error("Not implemented");
  }
  async findById(userId){
    throw new Error("Not implemented");
  }
}