import { UserRepository } from "../../domain/irepositories/UserRepository.js";
import { User } from "../../domain/entities/User.js"
import { Token } from "../../domain/entities/Token.js"

export class ApiUserRepository extends UserRepository{
    constructor(baseUrl = "http://localhost:3000") {
      super();
    this.baseUrl = baseUrl;
  }
    async validateToken(){

    }

    async login(email, username, password){
      const response = await fetch(`${this.baseUrl}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, email, password }) 
  });

      if(!response.ok){
        throw new Error("Invalid credentials!");
      }

      const data = await response.json();


  return {
    user: new User(data.username, data.email),
    token: new Token(data.token, data.expiresAt)
  };
    }

    async findById(){

    }
}