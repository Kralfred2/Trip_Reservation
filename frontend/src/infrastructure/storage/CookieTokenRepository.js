import { TokenRepository } from "../../domain/irepositories/TokenRepository.js";
import { Token } from "../../domain/entities/Token.js";

export class CookieTokenRepository extends TokenRepository {

  getToken() {
    const name = "token=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i].trim();
      if (c.indexOf(name) == 0) {
        return new Token(c.substring(name.length, c.length), null); 
      }
    }
    return null;
  }


saveToken(tokenObject) {
  const maxAge = tokenObject.expiresAt; 

  document.cookie = `token=${tokenObject.value}; max-age=${maxAge}; path=/; SameSite=Lax`;
}

  clearToken() {
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  }
}