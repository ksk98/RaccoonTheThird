# Raccoon The Third

Raccoon The Third is a scalable Discord bot application that I have developed for personal use. This is a public, base version of the application which I use as a foundation for the bot that works on my Discord server.

The project consists of 3 repositories:
- [Server](https://github.com/ksk98/RaccoonTheThird "Raccoon server") used to host the dockerized bot
- [Client](https://github.com/ksk98/RacoonClient "Raccoon client") that provides functionalities such as browsing server logs, reading discord messages and speaking as bot
- [Shared library](https://github.com/ksk98/RacoonShared "Raccoon shared") that is used both by the server and client

## Features
- Convenient base for creating new commands along with examples
- Command invocation both by slash interaction and text calls with customizable, multiple accepted prefixes
- Ability to use message reactions to upvote or downvote messages with persistent score tracking
- Scalable and secure JSON based client-server communication
- Integration with Spring, which provides bean context, profile-specific application properties and potential for addition of web functionalities
- Application property overloading via run parameters
- Proven to work in dockerized environment (tested on [Mogenius](https://mogenius.com))

## Deployment
### Cryptomaterial
The application requires a pair of asymmetrical keys to communicate with a remote client. Those keys are created via a keytool provided by java and stored in a keystore file. To do this, first navigate to the location of the keytool:
   ```
   cd %JAVA_HOME%\bin
   ```
Next, generate a key pair. Remember to change the value of the parameter `-keypass`, as this is the password that the `keystore.jks` will be secured with:
   ```
   keytool -genkey -alias raccoon-server -keyalg RSA -keysize 4096 -keypass changeit -keystore keystore.jks
   ```
Finally, use the generated keystore to generate a self signed certificate file containing public key of the server, which will be used by the client during SSL handshake:
   ```
   keytool -export -alias raccoon-server -keystore keystore.jks -rfc -file raccoon.cert
   ```
After creating necessary cryptomaterial, store `keystore.jks` somewhere accessible for the server app and provide its path as a launch parameter of the server app like so:
   ```
   --ssl.keystore.path=LOCATION_TO_KEYSTORE.JKS
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
   - `ssl.keystore.path` - path to `keystore.jks` file mentioned in section [Cryptomaterial](#cryptomaterial)
   - `ssl.keystore.password` - keystore password
   - `spring.datasource.url` - url for the database file will be stored (skip for `jdbc:h2:file:./spring-boot-h2-db-prod`)
   
Your final jar run command may look like this:
   ```
   java -jar Raccoon.jar --spring.profiles.active=prod --jda.token=$jda_token --ssl.keystore_path=/var/lib/data/keystore.jks --ssl.keystore_password=$keystore_password --spring.datasource.url=jdbc:h2:file:/var/lib/data/spring-boot-h2-db-prod
   ```
It is a preferable practice to inject both `jda.token` and `ssl.keystore.password` from some sort of secret variable.
Now when the application is up, it will listen for client connections on port specified in property `serversocket.port` (default 3435).


## Commands
Application supports both text commands (called with a prefix, keyword and optionally arguments) and slash commands (called by typing in / along with a command chosen from the displayed list.
Some commands will support only one of those methods, for example all commands that accept a number of arguments that is not constant will only be available with text calls. To obtain information about all or one particular command, use the command `help`.

Application can support multiple prefixes (`!` and `$` by default), editable in `Config.java`. Note that prefixes can only take form of a single character!

### Command creation
1. Create a new class that extends `Commands.Abstractions.Command.java`
2. Place a call to `super()` in the constructor, pass in the keyword and boolean flags stating which invocation methods the command will support
   - if command supports text calls, `supportsTextCalls` will be `true` and `executeImpl` method that accepts `MessageReceivedEvent` will be overloaded
   - if command supports interacion calls, `supportsInteractionCalls` will be `true` and `executeImpl` that accepts `SlashCommandInteractionEvent` will be overloaded
   - additionaly, if the command accepts arguments and supports interaction calls, `getCommandData` will be overloaded and it will return a custom `CommandDataImpl` containing argument requirements in the form of options
3. Assign a new `Commands.Abstractions.CommandInfo` object to field `info`, create the object with `Commands.Abstractions.CommandInfoBuilder`
4. Alter the commands behavior to your needs by customizing other built in fields if necessary
5. Add a new instance of created class in `Services.DiscordServices.CommandRelated.CommandService.java::loadCommands`. 

## Client-server communication
Client connections are accepted inside `Services.ClientServices.ServerSocketManager`. All communication goes trough `Services.ClientServices.SocketIOServices.TrafficService`. If a class will send data to clients, it should request `Services.ClientServices.SocketIOServices.IOutboundTrafficServiceUtilityWrapper` from spring context and use it to either broadcast information to all authorized clients or send data to a particular connection.

### Operation handling
Both client and the server state intention of their requests with a key `operation` that contains an enum value of `SocketCommunication.SocketOperationIdentifiers` imported from [Raccoon Shared](https://github.com/ksk98/RacoonShared "Raccoon shared") library. Request handling is processed by the handler chain composed of classes that extend `IncomingDataHandlers.JSONOperationHandler`. If a handling class wishes to send back data it is required to include key `client_operation_id` along with its value if one is present in incoming JSONObject. Data can be sent to the client by obtaining connection from previously mentioned `IOutboundTrafficServiceUtilityWrapper` with `connection_id` that will be injected by the `TrafficService` into incoming JSONOBject.

The handler chain is created in `Configuration.SocketCommunicationConfig::getTrafficHandlerChain` and used in `TrafficService`. It is setup by connecting handlers with their `setNext` method. Keep in mind that handlers that will likely be used the most should be placed in the beginning of the chain for better performance.
