package com.bots.RacoonServer.Configuration;

import com.bots.RacoonServer.Logging.LoggerSimple;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {
    private final Logger logger = new LoggerSimple();

    @Bean
    public Logger getLogger() {
        return logger;
    }
}
