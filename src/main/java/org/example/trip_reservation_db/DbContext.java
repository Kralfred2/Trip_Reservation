package org.example.trip_reservation_db;

import io.github.cdimascio.dotenv.Dotenv;



public class DbContext {


    Dotenv dotenv = Dotenv.load();
    String DB_URL = dotenv.get("DB_URL");
    String DB_USER = dotenv.get("DB_USER");
    String DB_PASS = dotenv.get("DB_PASS");


}
