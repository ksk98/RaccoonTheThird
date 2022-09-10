package com.bots.RacoonServer;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class RacoonApplication {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println(
					"Please enter JDA token and a jasypt secret, ex.:\n" +
					"java -jar bot.jar --jda.token=<JDA TOKEN> --jasypt.encryptor.password=<JASYPT SECRET> " +
							"--ssl.keystore_path=<PATH> --ssl.keystore_password=<PASSWORD>"
			);
			return;
		}

		SpringApplication.run(RacoonApplication.class, args);
	}
}
