import { UserRepository } from "../../domain/irepositories/UserRepository.js";
import { User } from "../../domain/entities/User.js"
import { Token } from "../../domain/entities/Token.js"

export class ApiUserRepository extends UserRepository{
    constructor(baseUrl = "http://localhost:3000") {
      super();
    this.baseUrl = baseUrl;
  }



async validateToken(tokenString) {
  const response = await fetch(`${this.baseUrl}/api/auth/validate`, {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${tokenString}`, 
      "Content-Type": "application/json"
    }
  });
console.log("Checking token: " + tokenString);

  return response.ok ? response.json() : null;
}

    async login(email, username, password) {
  const response = await fetch(`${this.baseUrl}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, username, password }) 
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || "Invalid credentials!");
  }


  const data = await response.json();
console.log("responce data: " + JSON.stringify(data));
  return {
  user: new User({
      id: data.userId,
      username: data.username, 
      email: data.email, 
      role: data.role, 
      permissions: data.permissions
  }),
  token: new Token(data.token, data.expiresAt)
};
}


    async findAll() {
    const response = await fetch(`${this.baseUrl}/users`);
    const data = await response.json();


    return data.map(rawUser => new User({
      id: rawUser.id,
      username: rawUser.username,
      email: rawUser.email,
      role: rawUser.role,
      permissions: item.perms || []
    }));
  }

    async findById(){

    }
}