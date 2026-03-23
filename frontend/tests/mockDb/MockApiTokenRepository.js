import { TokenRepository } from "../../src/domain/irepositories/TokenRepository.js";

export class MockApiUserRepository extends TokenRepository{

    async getToken(token){
       return { token: "mock-jwt-token-xyz" }
             
    }

    async saveToken(token){
       return { token: "mock-jwt-token-xyz" }
    }

    async clearToken(){

    }
}
