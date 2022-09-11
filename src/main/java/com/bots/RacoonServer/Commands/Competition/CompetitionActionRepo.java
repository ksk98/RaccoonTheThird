package com.bots.RacoonServer.Commands.Competition;

import java.util.List;
import java.util.Random;

// TODO: very simple for now but has a lot of potential to upgrade
public abstract class CompetitionActionRepo {
    private final static Random random = new Random();
    private final static List<CompetitionActionRunnable> actions = List.of(
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(random.nextInt(10, 20) * -1);
                    return "\uD83D\uDE34 " + target + " rests.\n";
                } else {
                    target.dealDamage(random.nextInt(10, 30));
                    return "\uD83D\uDCAA " + user + " punches " + target + "!\n" + giveDeathMessageIfDead(target);
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(random.nextInt(10, 20));
                    return "\uD83C\uDF4C " + target + " slips on a banana peel and falls!\n" + giveDeathMessageIfDead(target);
                } else {
                    target.dealDamage(random.nextInt(5, 35));
                    return "\uD83E\uDDB5 " + user + " kicks " + target + "!\n" + giveDeathMessageIfDead(target);
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(999);
                    return "\uD83D\uDCA5 " + target + " steps on a mine!\n" + giveDeathMessageIfDead(target);
                } else {
                    target.dealDamage(random.nextInt(5, 10));
                    return "\uD83D\uDCA2 " + user + " insults " + target + "!\n" +
                            (target.isDead() ? "\n\uD83D\uDE2D " + target + " cries and runs home to mama!\n" : "");
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    int roll = random.nextInt(3);
                    if (roll == 0)
                        target.dealDamage(random.nextInt(10, 40));
                    else if (roll == 1)
                        target.dealDamage(20);
                    // else nothing happens

                    return "\uD83C\uDF4E " + target + " eats a mysterious fruit.\n" + giveDeathMessageIfDead(target);
                } else {
                    user.dealDamage(random.nextInt(15, 25) * -1);
                    return "\uD83E\uDD9D " + user + " steals food from " + target + "!\n";
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(999);
                    return "\uD83C\uDF57 " + target + " has to go home because of dinner.\n";
                } else {
                    int roll = random.nextInt(100);
                    if (roll < 20) {
                        user.dealDamage(999);
                        target.dealDamage(999);
                        return "\uD83C\uDF5D " + user + " decides to invite " + target + " for lunch. " +
                                target + " agrees and they are both leaving.\n";
                    } else {
                        user.dealDamage(10);
                        return "\uD83D\uDE2C " + user + " decides to invite " + target + " for lunch. " +
                                target + " calls " + user + " a buffon!\n" + giveDeathMessageIfDead(target);
                    }

                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(25);
                    return "\uD83D\uDC74 " + target + " is old, so naturally their body starts to hurt randomly.\n" + giveDeathMessageIfDead(target);
                } else {
                    target.dealDamage(random.nextInt(30, 50));
                    return "\uD83D\uDE31 " + user + " scares " + target + ", giving them a heart attack.\n" + giveDeathMessageIfDead(target);
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(10);
                    return "\uD83D\uDE1F " + target + " has an existential crisis.\n" + giveDeathMessageIfDead(target);
                } else {
                    target.dealDamage(random.nextInt(15, 30));
                    return "\uD83D\uDCA2 " + user + " smacks " + target + " with a stick!\n" + giveDeathMessageIfDead(target);
                }
            }
    );

    public static CompetitionActionRunnable getRandomAction() {
        return actions.get(random.nextInt(actions.size()));
    }

    private static String giveDeathMessageIfDead(CompetitionContestant target) {
        return (target.isDead() ? "⠀⠀⠀⠀☠️" + target + " dies!\n" : "");
    }
}
