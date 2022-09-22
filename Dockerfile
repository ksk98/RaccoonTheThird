FROM openjdk:17
RUN /gradlew.bat bootjar

ARG USERNAME=raccoon
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME
RUN useradd --uid $USER_UID --gid $USER_GID -m $USERNAME
RUN chmod +x Raccoon.jar

USER root
ENTRYPOINT ["sh", "-c", "java -jar Raccoon.jar --spring.profiles.active=prod --jda.token=$jda_token --jasypt.encryptor.password=$jasypt_password --ssl.keystore_path=/var/lib/data/keystore.jks --ssl.keystore_password=$keystore_password"]
