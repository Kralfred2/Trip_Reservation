package org.example.reservation_api.DTO;

public class LoginResponse {
    private boolean success;
    private String token;
    private String message;

    public LoginResponse(boolean success, String token, String message) {
        this.success = success;
        this.token = token;
        this.message = message;
    }

    // Getters and Setters (Important for JSON serialization!)
    public boolean isSuccess() { return success; }
    public String getToken() { return token; }
    public String getMessage() { return message; }
}
