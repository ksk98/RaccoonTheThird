package com.bots.RacoonServer.Commands.Competition;

import com.bots.RacoonServer.Commands.Abstractions.CommandBase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandCompetition extends CommandBase {
    public CommandCompetition() {
        super("competition", "Simulate a match of hunger games between given contestants.",
                true, false);
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
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
