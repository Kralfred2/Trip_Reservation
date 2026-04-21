// infrastructure/API/BaseApiRepository.js
import { UserRepository } from "../../domain/irepositories/UserRepository.js";

// We extend the domain repo so this class "counts" as a UserRepository
export class BaseApiRepository extends UserRepository {
    constructor(baseUrl) {
        super(); 
        this.baseUrl = baseUrl;
        this.token = null;
    }

setToken(token) {
    // If it's an object, extract the value string; otherwise, use the string directly
    const tokenString = (token && typeof token === 'object') ? token.value : token;
    
    console.warn("BaseRepo: Storing raw token string: " + tokenString?.substring(0, 10) + "...");
    this.token = tokenString;
}

    async request(path, options = {}) {
        const url = `${this.baseUrl}${path}`;
        

        const headers = {
            "Content-Type": "application/json",
            ...options.headers
        };

        if (this.token) {
            headers["Authorization"] = `Bearer ${this.token}`;
        }

        const response = await fetch(url, {
            ...options,
            headers: headers
        });

        if (!response.ok) {
            const errorBody = await response.json().catch(() => ({}));
            throw new Error(errorBody.message || `API Error: ${response.status}`);
        }

        return response.json();
    }
}