package com.bots.RaccoonServer.Commands.Entertainment.MineSweeper;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;
import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandMinesweeper extends Command {
    private final String BOMB_COUNT_OPTION_NAME = "bomb_count";

    // Although printing the game in an embed looks a lot nicer
    // fields are smaller and therefore harder to click on.
    // In addition to that, many games printed in an embed
    // can make discord lag.

    public CommandMinesweeper() {
        super("minesweeper", true, true);
        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Generates a simple game of minesweeper.")
                .setCategory(CommandCategory.ENTERTAINMENT);

        this.info = builder.build(this);
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        int bombs;

        try {
            bombs = Integer.parseInt(arguments.get(0));
            event.getChannel().sendMessage(new MinesweeperGame(bombs).toString()).queue();
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            event.getChannel().sendMessage(new MinesweeperGame().toString()).queue();
        }
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.getInteraction().reply(
                new MinesweeperGame(
                        event
                                .getInteraction()
                                .getOption(BOMB_COUNT_OPTION_NAME, MinesweeperGame.BOMBS_DEFAULT, OptionMapping::getAsInt)
                ).toString()
                ).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl(keyword, info.getSimpleDescription())
                .addOption(OptionType.INTEGER, BOMB_COUNT_OPTION_NAME,
                        "Amount of bombs between " + MinesweeperGame.BOMBS_MIN + " and " + MinesweeperGame.BOMBS_MAX,
                        false);
    }
}
