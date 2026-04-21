import { BaseApiRepository } from "./BaseApiRepository.js";
import { User } from "../../domain/entities/User.js";
import { Token } from "../../domain/entities/Token.js";

export class ApiUserRepository extends BaseApiRepository {
  constructor(baseUrl) {
    super(baseUrl);
  }


  async login(email, username, password) {
    const data = await this.request("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, username, password })
    });

    // Ensure we are passing the raw string to the base repository
    const tokenString = data.token.token || data.token;
    this.setToken(tokenString); 

    return {
        user: new User({
            id: data.id,
            username: data.username,
            email: data.email,
            role: data.role,
            permissions: data.permissions
        }),
        token: new Token(tokenString, data.token.expiresAt || data.expiresAt)
    };
}

async validateToken(tokenString) {

const data = await this.request("/api/auth/validate", {
      method: "POST",
      headers: {
      "Authorization": `Bearer ${tokenString}`, 
      "Content-Type": "application/json"
    }
    });

      return {
        user: new User({
            id: data.id,
            username: data.username,
            email: data.email,
            role: data.role,
            permissions: data.permissions
        }),
        token: new Token(tokenString, data.token.expiresAt || data.expiresAt)
    };
}

async getAllUsers() {
    // FIX: Use the inherited request helper. 
    // This is why you don't see a network request: the previous code crashed here.
    return await this.request("/api/users", {
        method: "GET"
    });
  }
}