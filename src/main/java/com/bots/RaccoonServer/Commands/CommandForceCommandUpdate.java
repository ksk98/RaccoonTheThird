package com.bots.RaccoonServer.Commands;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.SpringContext;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandForceCommandUpdate extends Command {
    public CommandForceCommandUpdate() {
        super("force_command_update", true, true);

        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Forces command synchronisation.");

        this.info = builder.build(this);
        this.adminCommand = true;
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        SpringContext.getBean(CommandService.class).syncSlashCommands();
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        SpringContext.getBean(CommandService.class).syncSlashCommands();
    }
}
