package com.bots.RacoonServer.Commands.Abstractions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;

public interface Command extends Serializable {
    /***
     * Execute the command in response to a call present in a received message.
     * @param event message event in which the call was made
     * @param arguments list of text arguments provided for the command
     */
    void execute(@NonNull MessageReceivedEvent event, @NonNull List<String> arguments);

    /***
     * Execute the command in response to a call present in a slash command.
     * @param event interaction event in which the call was made
     */
    void execute(@NonNull SlashCommandInteractionEvent event);


    /***
     * @return command data to be used in publishing slash commands, null if slash command is not supported
     */
    CommandData getCommandData();

    /***
     * @return a keyword used to call the command
     */
    String getKeyword();

    /***
     * @return description of the command
     */
    String getDescription();

    /***
     * @return true if command should be callable from a text message via keyword
     */
    boolean isTextCommand();

    /***
     * @return true if command should be callable via a slash command interaction
     */
    boolean isSlashCommand();

    /***
     * @return true if text command call message should be deleted
     */
    boolean deleteMessageAfterUse();
}
