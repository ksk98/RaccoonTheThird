# RaccoonTheThird
**This project is work in progress, everything is subject to change!**

Raccoon The Third is a scalable Discord bot platform that I'm developing for personal use.

This project consists of 3 repositories:
- [Server](https://github.com/ksk98/RaccoonTheThird "Raccoon server") used to host the dockerized bot
- [Client](https://github.com/ksk98/RacoonClient "Raccoon client") that provides functionalities such as browsing server logs, discord messages and speaking as bot
- [Shared library](https://github.com/ksk98/RacoonShared "Raccoon shared") that is used both by the server and client

## Features
- Built in example commands used for entertainment along with convenient base for creation of new commands
- Ability to use message reactions to upvote or downvote messages with persistent score tracking
- Scalable method of client-server communication handling
- Integration with Spring, which provides bean context, profile-specific application properties and potential for addition of web functionalities
- SSLSocket based communication

## Deployment
### Cryptomaterial
The application requires a pair of asymmetrical keys to communicate with a remote client. Those keys are created via a keytool provided by java and stored in a keystore file. To do this, first navigate to the location of the keytool:
   ```
   cd %JAVA_HOME%\bin
   ```
Next, generate the key pair. Remember to change the value of the parameter `-keypass`, as this is the password that the `keystore.jks` will be secured with:
   ```
   keytool -genkey -alias raccoon-server -keyalg RSA -keysize 4096 -keypass changeit -keystore keystore.jks
   ```
Finally, use the generated keystore to generate a self signed certificate file containing public key of the server, which will be used by the client during SSL handshake:
   ```
   keytool -export -alias raccoon-server -keystore keystore.jks -rfc -file raccoon.cert
   ```
After creating necessary cryptomaterial, store `keystore.jks` somewhere accessible for the server app and provide its path as a launch parameter of the server app like so:
   ```
   --ssl.keystore_password=LOCATION_TO_KEYSTORE.JKS
   ```
As for `raccoon.cert`, place it in the same directory as the client application.

### Launch
To launch the application locally, a jar file will have to be built first. This can be done with gradle's bootjar task in the root catalog of the project:
   ```
   gradle bootjar
   ```
Alternatively, if gradle is not installed on your machine, run the task with gradlew script:
   ```
   ./gradlew bootjar
   ```
The jar will be located at `./build/libs`.
To connect to the app from the client locally, connect to address localhost with a port assigned to property `serversocket.port` in the `application.properties` file (default 3435).

### Launch parameters
The project supports overloading of application properties via jar launch parameters.
To overload a property, enter a parameter structured like so:
   ```
   --parameter.path=value
   ```
after calling `java -jar`. Keep in mind that while you can optionally override any property, some properties are mandatory to override when running the jar.
Those properties are:
   - `spring.profiles.active` - choose either `dev` or `prod`
   - `jda.token` - token of the Discord bot that the app will control
   - `ssl.keystore_path` - path to `keystore.jks` file mentioned in section [Cryptomaterial](#cryptomaterial)
   - `ssl.keystore_password` - keystore password
   - `spring.datasource.url` - path where the database file will be stored (leave blank for `./spring-boot-h2-db-prod`)
Your final jar run command may look like this:
   ```
   java -jar Raccoon.jar --spring.profiles.active=prod --jda.token=$jda_token --ssl.keystore_path=/var/lib/data/keystore.jks --ssl.keystore_password=$keystore_password --spring.datasource.url=jdbc:h2:file:/var/lib/data/spring-boot-h2-db-prod
   ```
It is a preferable practice to inject both `jda.token` and `ssl.keystore_password` from some sort of secret variable.


## Creating a command
1. Create a new command class under **com.bots.RaccoonServer.Commands**, command class should extend **com.bots.RaccoonServer.Commands.Abstractions.CommandBase**
2. Define command's constructor which has to call constructor of the parent class with parameters stating information about created command:
   - keyword - string alias that the command will be identified from. Keyword has to be a single word that does not contain whitespace or quotation marks (double and single)
   - description - text description that states what the command does
   - supportsTextCalls - true if command will support invoking via message received in a text channel that the bot is in
   - supportsInteractionCalls - true if command will support invoking via a slash command interaction
 
 3. Define command's behavior:
 
    - If command supports text calls, override method **execute** that accepts **MessageReceivedEvent** and a list of String arguments. Arguments list will contain everything that's written after the command call, where words grouped by quotes will count as one string.
    - If command supports slash command calls, override method **execute** that accepts **SlashCommandInteractionEvent**. Additionally, if your command accepts arguments, override method **getCommandData**, which should return instance of **CommandDataImpl** containing information about your commands arguments. This is called when slash commands that the bot supports have changed and need to be synchronised with Discord.
    - Optionally, change the default value of **CommandBase** flags to alter the command's behavior to your liking

4. Add command to the list of commands in **com.bots.RaccoonServer.Services.CommandService.loadCommands()**
