package com.cardosofiles.nubank_challenge_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.cardosofiles.nubank_challenge_server.config.CorsProperties;

@SpringBootApplication
@EnableConfigurationProperties(CorsProperties.class)

public class NubankChallengeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NubankChallengeServerApplication.class, args);
	}

}
