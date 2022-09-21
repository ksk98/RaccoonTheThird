FROM openjdk:17
COPY ./artifact/ /wdir
WORKDIR /wdir
ENTRYPOINT ["sh", "-c", "java -jar Racoon.jar --spring.profiles.active=prod --jda.token=$jda_token --jasypt.encryptor.password=$jasypt_password --ssl.keystore_path=/mo-data/keystore.jks --ssl.keystore_password=$keystore_password"]
