// infrastructure/api/MockApiUserRepository.js
import { UserRepository } from "../../src/domain/irepositories/UserRepository.js";
import { User } from "../../src/domain/entities/User.js";

export class MockApiUserRepository extends UserRepository {
  async login(email, password) {
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 500));

    // Hardcoded check for testing purposes
    if (email === "test@example.com" && password === "password123") {
      return {
        user: new User(1, "test@example.com"), //
        token: "mock-jwt-token-xyz"
      };
    }

    throw new Error("Unauthorized");
  }

  async validateToken(token) {
    if (token === "mock-jwt-token-xyz") {
      return new User(1, "test@example.com"); //
    }
    return null; //
  }


}