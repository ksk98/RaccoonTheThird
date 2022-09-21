FROM openjdk:17
COPY ./artifact /wdir
WORKDIR /wdir
USER 101:101
ENTRYPOINT ["sh", "-c", "java -jar Racoon.jar --spring.profiles.active=prod --jda.token=$jda_token --jasypt.encryptor.password=$jasypt_password --ssl.keystore_path=/var/lib/data/ssl/keystore.jks --ssl.keystore_password=$keystore_password"]
