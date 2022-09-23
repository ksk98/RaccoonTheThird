FROM gradle:7.5.1-jdk17

COPY . /RaccoonServer
WORKDIR /RaccoonServer

# Create Raccoon.jar
RUN gradle bootjar

ENTRYPOINT ["sh", "-c", "java -jar Raccoon.jar --spring.profiles.active=prod --jda.token=$jda_token --jasypt.encryptor.password=$jasypt_password --ssl.keystore_path=/var/lib/data/keystore.jks --ssl.keystore_password=$keystore_password"]
