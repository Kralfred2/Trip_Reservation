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
      const tokenValue = c.substring(name.length, c.length);
      console.log("Token found in cookie by repository:", tokenValue); 
      return new Token(tokenValue, null); 
    }
  }
  console.log("No token found in cookies by repository.");
  return null;
}


saveToken(tokenObject) {
  const maxAge = tokenObject.expiresAt; 
  console.log("Repository saving token:");
  document.cookie = `token=${tokenObject.value}; max-age=${maxAge}; path=/; SameSite=Lax`;
}

  clearToken() {
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  }
}