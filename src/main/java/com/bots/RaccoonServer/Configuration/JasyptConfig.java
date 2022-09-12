package com.bots.RaccoonServer.Configuration;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.ApplicationArguments;

//@Configuration
public class JasyptConfig {
    private final String password;
    private final StandardPBEStringEncryptor encryptor;

//    @Autowired
    public JasyptConfig(ApplicationArguments arguments) {
        this.password = arguments.getNonOptionArgs().get(1);
        this.encryptor = new StandardPBEStringEncryptor();
        this.encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        this.encryptor.setPassword(password);
    }

//    @Bean
    public StandardPBEStringEncryptor getEncryptor() {
        return encryptor;
    }
}
