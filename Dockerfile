FROM gradle:7.5.1-jdk17

COPY . /RaccoonServer
WORKDIR /RaccoonServer

# Create Raccoon.jar
RUN gradle bootjar

ARG DATA=/var/lib/data
ARG PROFILE=prod
# ARG jda_token provided by host
ARG KEYSTORE=$DATA/keystore.jks
# ARG keystore_password provided by host
ARG DB=$DATA/spring-boot-h2-db-prod

ENTRYPOINT ["sh", "-c", "java -jar Raccoon.jar --spring.profiles.active=$PROFILE --jda.token=$jda_token --ssl.keystore_path=$KEYSTORE --ssl.keystore_password=$keystore_password --spring.datasource.url=jdbc:h2:file:$DB"]
