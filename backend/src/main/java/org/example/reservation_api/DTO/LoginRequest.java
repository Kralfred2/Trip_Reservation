package org.example.reservation_api.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter // Applies to all fields
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String username;
    private String password;


}
