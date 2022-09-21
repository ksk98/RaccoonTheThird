package com.bots.RaccoonServer.Commands.Entertainment.Competition;

@FunctionalInterface
public interface CompetitionActionRunnable {
    String perform(CompetitionContestant user, CompetitionContestant target);
}
