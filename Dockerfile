FROM openjdk:17
COPY ./artifact/ /wdir
WORKDIR /wdir
ENTRYPOINT ["java", "-jar", "Racoon.jar","--spring.profiles.active=prod","--jda.token=$jda_token" ,"--jasypt.encryptor.password=$encryptor_password","--ssl.keystore_path=keystore.jks","--ssl.keystore_password=$keystore_password"]
