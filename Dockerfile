FROM openjdk:17

COPY ./artifact/Racoon.jar Racoon.jar

ARG USERNAME=raccoon
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME
RUN useradd --uid $USER_UID --gid $USER_GID -m $USERNAME
RUN chmod +x Racoon.jar

USER $USERNAME
ENTRYPOINT ["java", "-jar", "/Racoon.jar"]
CMD ["--spring.profiles.active=prod", "--jda.token=$jda_token", "--jasypt.encryptor.password=$jasypt_password", "--ssl.keystore_path=/var/lib/data/ssl/keystore.jks", "--ssl.keystore_password=$keystore_password"]
