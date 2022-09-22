FROM openjdk:17
COPY ./artifact /var/lib/data
WORKDIR /var/lib/data
USER 101:101
ENTRYPOINT ["sh", "-c", "java -jar Racoon.jar --spring.profiles.active=prod --jda.token=$jda_token --jasypt.encryptor.password=$jasypt_password --ssl.keystore_path=/var/lib/data/ssl/keystore.jks --ssl.keystore_password=$keystore_password"]
