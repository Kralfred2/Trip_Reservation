package org.example.reservation_api.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security")
@Getter
@Setter
public class AppSecurityProperties {
    private Password password = new Password();
    private Username username = new Username();

    @Getter @Setter
    public static class Password {
        private int minLength = 8;
        private boolean requireSpecialChars = false;
    }

    @Getter @Setter
    public static class Username {
        private int minLength = 3;
        private int maxLength = 50;
    }

}
