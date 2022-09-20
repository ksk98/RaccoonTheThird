package com.bots.RaccoonServer.Commands.Competition;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;
import com.bots.RaccoonServer.Commands.Abstractions.Description.CommandInfoBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandCompetition extends Command {
    private final int minPlayers = 2, maxPlayers = 6;

    public CommandCompetition() {
        super("competition",true, false);

        CommandInfoBuilder descriptionBuilder =
                new CommandInfoBuilder()
                        .setSimpleDescription("Simulate a match of hunger games between " + minPlayers + " to " + maxPlayers + " contestants.")
                        .setExamples(getKeyword() + " 'James Hetfield' Madonna \"Marvin Gaye\"");
        info = descriptionBuilder.build();
        category = CommandCategory.ENTERTAINMENT;

        // TODO: do this^ for the rest + detailed description help command
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        if (arguments.size() < minPlayers || arguments.size() >= maxPlayers) {
            event.getChannel()
                    .sendMessage("Player amount in a competition must be between " + minPlayers + " and " + maxPlayers + ".")
                    .queue();
            return;
        }

        Random random = new Random();
        List<CompetitionContestant> contestants = arguments.stream().map(CompetitionContestant::new).collect(Collectors.toCollection(LinkedList::new));
        Queue<CompetitionContestant> movementQueue = new LinkedList<>();
        StringBuilder story = new StringBuilder();

        story.append("⚔️Game starts with ")
                .append(contestants.size())
                .append(" contestants!⚔️\n\n");

        int round = 1;
        while (contestants.size() > 1) {
            movementQueue.addAll(contestants);
            story.append("ROUND ").append(round).append("\n\n");

            while (movementQueue.size() > 1) {

                CompetitionContestant currentPlayer = movementQueue.poll();
                CompetitionContestant targetPlayer = contestants.get(random.nextInt(contestants.size()));

                story.append(CompetitionActionRepo.getRandomAction().perform(currentPlayer, targetPlayer))
                        .append("\n");

                boolean death = false;
                if (currentPlayer.isDead()) {
                    contestants.remove(currentPlayer);
                    death = true;
                }

                if (targetPlayer.isDead()) {
                    contestants.remove(targetPlayer);
                    movementQueue.remove(targetPlayer);
                    death = true;
                }

                if (death && contestants.size() > 1) {
                    story.append(contestants.size()).append(" contestants alive!\n\n");
                }
            }

            round += 1;
        }

        if (contestants.size() > 0)
            story.append("\uD83C\uDFC6 ").append(contestants.get(0).getName()).append(" wins! \uD83C\uDFC6");
        else
            story.append("Nobody wins!");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Competition");
        embedBuilder.setDescription(story.toString());

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
