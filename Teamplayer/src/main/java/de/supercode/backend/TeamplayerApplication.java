package de.supercode.backend;

import de.supercode.backend.configurations.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class TeamplayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamplayerApplication.class, args);
    }

}
