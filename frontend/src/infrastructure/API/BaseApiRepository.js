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
        console.warn("updating token setToken: " + JSON.stringify(token));
        this.token = token;
    }

    /**
     * Core helper to build and send requests
     */
    async request(path, options = {}) {
        const url = `${this.baseUrl}${path}`;
        
        // Merge provided headers with standard JSON and Auth headers
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

        // Basic error handling for 401, 403, 500 etc.
        if (!response.ok) {
            const errorBody = await response.json().catch(() => ({}));
            throw new Error(errorBody.message || `API Error: ${response.status}`);
        }

        return response.json();
    }
}