package com.bots.RacoonServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class RacoonApplication {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Please enter JDA token as the first argument.");
			return;
		}

		ConfigurableApplicationContext applicationContext =  SpringApplication.run(RacoonApplication.class, args);
		if (!applicationContext.getBean(JdaManager.class).initialise(args[0]))
			applicationContext.close();
	}

}
