package org.example.reservation_api.DTO;

import java.util.UUID;

public record UserListResponse(

        UUID id,
        String username,
        boolean hasMoreDetails

) {
}
