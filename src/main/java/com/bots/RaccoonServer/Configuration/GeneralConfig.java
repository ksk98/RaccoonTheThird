package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.Logging.RacoonLogger;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class GeneralConfig {
    private final ILogger logger;

    public GeneralConfig(Environment environment, GenericOnCreatePublisher<IOutboundTrafficServiceUtilityWrapper> trafficServiceWrapperCreationPublisher) {
        validateMandatoryParameterOverloads(environment);
        logger = new RacoonLogger(trafficServiceWrapperCreationPublisher);
    }

    @Bean
    public ILogger getLogger() {
        return logger;
    }

    private static void validateMandatoryParameterOverloads(Environment environment) {
        try {
            String profile = Objects.requireNonNull(environment.getProperty("spring.profiles.active"));
            if (profile.equals("default")) throw new NullPointerException();

            Objects.requireNonNull(environment.getProperty("jda.token"));
            Objects.requireNonNull(environment.getProperty("ssl.keystore.path"));
            Objects.requireNonNull(environment.getProperty("ssl.keystore.password"));
        } catch (NullPointerException ignored) {
            System.out.println("Please overload mandatory properties when running the application!");
            System.out.println("Example usage: \n" +
                    "java -jar Raccoon.jar " +
                    "--spring.profiles.active=prod " +
                    "--jda.token=TOKEN " +
                    "--ssl.keystore_path=keystore.jks " +
                    "--ssl.keystore_password=PASSWORD " +
                    "--spring.datasource.url=jdbc:h2:file:./db-name");
            System.exit(1);
        }
    }
}
