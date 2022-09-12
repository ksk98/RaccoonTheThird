package com.bots.RaccoonServer.Commands.Competition;

@FunctionalInterface
public interface CompetitionActionRunnable {
    String perform(CompetitionContestant user, CompetitionContestant target);
}
