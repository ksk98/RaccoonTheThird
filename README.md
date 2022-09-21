# RaccoonTheThird
**This project is work in progress, everything is subject to change!**

Raccoon The Third is a scalable Discord bot platform that I'm developing for personal use.

This project consists of 3 repositories:
- [Server](https://github.com/ksk98/RaccoonTheThird "Raccoon server") used to host the dockerized bot
- [Client](https://github.com/ksk98/RacoonClient "Raccoon client") that provides functionalities such as browsing server logs, discord messages and speaking as bot
- [Shared library](https://github.com/ksk98/RacoonShared "Raccoon shared") that is used both by the server and client

## Features
- Built in example commands used for entertainment and simple command creation
- Ability to use reactions to upvote or downvote messages with persistent score tracking
- Scalable method of client-server communication handling
- Integration with Spring, which provides bean context, profile-specific application properties and potential for addition of web functionalities
- SSLSocket based communication and optional application property encryption with Jasypt

## Deployment
  **TODO**
  Generate keystore
  cd %JAVA_HOME%\bin
  keytool -genkey -alias racoon-server -keyalg RSA -keysize 4096 -keypass changeit -keystore keystore.jks
  keytool -export -alias racoon-server -keystore keystore.jks -rfc -file racoon.cert
  Run with parameters
  client prod default password  encryptedpassword2448!

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
