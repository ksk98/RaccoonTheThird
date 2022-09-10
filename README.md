Generate keystore

cd %JAVA_HOME%\bin
keytool -genkey -alias racoon-server -keyalg RSA -keysize 4096 -keypass changeit -keystore keystore.jks
keytool -export -alias racoon-server -keystore keystore.jks -rfc -file racoon.cert

Run with parameters

client prod default password  encryptedpassword2448!