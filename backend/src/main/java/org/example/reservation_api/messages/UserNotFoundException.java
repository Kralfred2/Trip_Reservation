package org.example.reservation_api.messages;

import lombok.NoArgsConstructor;



@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
