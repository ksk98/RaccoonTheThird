package com.bots.RaccoonServer.Commands.Abstractions;

import com.bots.RaccoonServer.Commands.Abstractions.Description.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public interface ICommand extends Serializable {
    /***
     * Execute the command in response to a call present in a received message.
     * @param event message event in which the call was made
     * @param arguments list of text arguments provided for the command
     */
    void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments);

    /***
     * Execute the command in response to a call present in a slash command.
     * @param event interaction event in which the call was made
     */
    void execute(@NotNull SlashCommandInteractionEvent event);

    /***
     * @return command data to be used in publishing slash commands, null if slash command is not supported
     */
    CommandData getCommandData();

    /***
     * @return a keyword used to call the command
     */
    String getKeyword();

    /***
     * Get info containing descriptions, use examples and category.
     * @return description of the command
     */
    CommandInfo getInfo();

    /***
     * @return true if command should be callable from a text message via keyword
     */
    boolean isTextCommand();

    /***
     * @return true if command should be callable via a slash command interaction
     */
    boolean isSlashCommand();

    /***
     * @return true if command is only available to an admin
     */
    boolean isAdminCommand();

    /***
     * @return true if text command call message should be deleted
     */
    boolean deleteMessageAfterUse();
}
