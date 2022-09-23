FROM gradle:7.5.1-jdk17

COPY . /RaccoonServer
WORKDIR /RaccoonServer

# Create Raccoon.jar
RUN gradle bootjar

ENTRYPOINT ["sh", "-c", "java -jar Raccoon.jar \
--spring.profiles.active=prod \
--jda.token=$jda_token \
--ssl.keystore_path=/var/lib/data/keystore.jks \
--ssl.keystore_password=$keystore_password \
--spring.datasource.url=jdbc:h2:file:/var/lib/data/spring-boot-h2-db-prod"]
