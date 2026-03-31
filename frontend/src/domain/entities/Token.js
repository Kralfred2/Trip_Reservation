
export class Token {
  constructor(value, expiresAt) {
    this.value = value;        
    this.expiresAt = expiresAt; 
  }
  
  isExpired() {
    return Date.now() > this.expiresAt;
  }

  getExpirationDate() {
    return new Date(this.expiresAt).toLocaleString();
  }
}