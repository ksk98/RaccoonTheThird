package com.bots.RacoonServer;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
@EnableEncryptableProperties
public class RacoonApplication {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println(
					"Please enter JDA token and a jasypt secret, ex.:\n" +
					"java -jar bot.jar --jda.token=<JDA TOKEN> --jasypt.encryptor.password=<JASYPT SECRET>"
			);
			return;
		}

		SpringApplication.run(RacoonApplication.class, args);
	}
}
