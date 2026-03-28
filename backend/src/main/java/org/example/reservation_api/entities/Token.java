package org.example.reservation_api.entities;

import java.util.UUID;

public class Token extends BaseEntity{

    private UUID ownerId;
    private boolean valid;
    private String value;

}
