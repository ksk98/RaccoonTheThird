package com.bots.RacoonServer.Commands.Competition;

import com.bots.RacoonServer.Commands.Abstractions.CommandBase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class CommandCompetition extends CommandBase {
    public CommandCompetition() {
        super("competition", "Simulate a match of hunger games between given contestants.",
                true, false);
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        Random random = new Random();
        List<CompetitionContestant> contestants = arguments.stream().map(CompetitionContestant::new).toList();
        Queue<CompetitionContestant> movementQueue = new LinkedList<>();
        StringBuilder story = new StringBuilder();

        while (contestants.size() > 1) {
            if (movementQueue.isEmpty())
                movementQueue.addAll(contestants);

            CompetitionContestant currentPlayer = movementQueue.poll();
            CompetitionContestant targetPlayer = contestants.get(random.nextInt(contestants.size()));

            story.append(CompetitionActionRepo.getRandomAction().perform(currentPlayer, targetPlayer))
                    .append("\n");

            if (currentPlayer.isDead())
                contestants.remove(currentPlayer);
            else
                movementQueue.add(currentPlayer);

            if (targetPlayer.isDead()) {
                contestants.remove(targetPlayer);
                movementQueue.remove(targetPlayer);
            }
        }

        story.append(contestants.get(0).getName())
                .append(" wins!");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Competition");
        embedBuilder.setDescription(story.toString());

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
