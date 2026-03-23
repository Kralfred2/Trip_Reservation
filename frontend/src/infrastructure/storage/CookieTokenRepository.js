import { TokenRepository } from "../../domain/irepositories/TokenRepository.js";

export class CookieTokenRepository extends TokenRepository {

  getToken() {
    return document.cookie
      .split("; ")
      .find(row => row.startsWith("token="))
      ?.split("=")[1];
  }

  saveToken(token) {
    document.cookie = `token=${token}; path=/`;
  }

  clearToken() {
    document.cookie = "token=; Max-Age=0";
  }

}