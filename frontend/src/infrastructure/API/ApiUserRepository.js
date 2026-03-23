import { UserRepository } from "../../domain/irepositories/UserRepository.js";
import { User } from "../../domain/entities/User.js"


export class ApiUserRepository extends UserRepository{

    async validateToken(){

    }

    async login(email, password){
      const response = await fetch ("api/login",{
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({email, password})
      });

      if(!response.ok){
        throw new Error("Invalid credentials!");
      }

      const responseData = await response.json();
    return {
        user: new User(responseData.id, responseData.email),
        token: responseData.token
    }
    }
}