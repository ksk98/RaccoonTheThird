package com.bots.RacoonServer.Commands.Competition;

@FunctionalInterface
public interface CompetitionActionRunnable {
    String perform(CompetitionContestant user, CompetitionContestant target);
}
