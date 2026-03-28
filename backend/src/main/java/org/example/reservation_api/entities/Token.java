package org.example.reservation_api.entities;

import java.util.UUID;

public class Token extends BaseEntity{

    private UUID ownerId;
    private boolean blackListed;


   public Token(
           UUID uuid,
           boolean val,
   ){
       this.ownerId = uuid;
       this.blackListed = val;

   }



}
