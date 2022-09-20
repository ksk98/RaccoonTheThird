package com.bots.RaccoonServer.Commands.Entertainment;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;
import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Command8Ball extends Command {
    private final String[] options = new String[] {
            "It is certain.", "It is decidedly so.", "Without a doubt.", "Yes definitely.", "You may rely on it.",
            "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.", "Signs point to yes.",
            "Reply hazy, try again.", "Ask again later.", "Better not tell you now.", "Cannot predict now.",
            "Concentrate and ask again.", "Don't count on it.", "My reply is no.", "My sources say no.",
            "Outlook not so good.", "Very doubtful."
    };

    public Command8Ball() {
        super("8ball", true, true);

        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Simulates a magic 8-ball styled decision making.")
                .setCategory(CommandCategory.ENTERTAINMENT);

        this.info = builder.build(this);
    }

    private String decide() {
        Random random = new Random();
        return options[random.nextInt(options.length)];
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        event.getChannel().sendMessage(decide()).queue();
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.getInteraction().reply(decide()).queue();
    }
}
