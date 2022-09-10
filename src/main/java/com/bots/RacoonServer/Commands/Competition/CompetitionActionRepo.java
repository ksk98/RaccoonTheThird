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
                    return target.getName() + " rests.";
                } else {
                    target.dealDamage(random.nextInt(10, 30));
                    return user.getName() + " punches " + target.getName() + "!" +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(random.nextInt(10, 20));
                    return target.getName() + " slips on a banana peel and falls!" +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                } else {
                    target.dealDamage(random.nextInt(5, 35));
                    return user.getName() + " kicks " + target.getName() + "!" +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(999);
                    return target.getName() + " steps on a mine!" +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                } else {
                    target.dealDamage(random.nextInt(5, 10));
                    return user.getName() + " insults " + target.getName() + "!" +
                            (target.isDead() ? "\n" + target.getName() + " cries and runs home to mama!" : "");
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

                    return target.getName() + " eats a mysterious fruit." +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                } else {
                    user.dealDamage(random.nextInt(15, 25) * -1);
                    return user.getName() + " steals food from " + target.getName() + "!";
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(999);
                    return target.getName() + " has to go home because of dinner.";
                } else {
                    user.dealDamage(999);
                    target.dealDamage(999);
                    return user.getName() + " decides to invite " + target.getName() + " for lunch. " +
                            target.getName() + " agrees and they are both leaving.";
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(25);
                    return target.getName() + " is old, so naturally their body starts to hurt randomly." +
                        (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                } else {
                    target.dealDamage(random.nextInt(30, 50));
                    return user.getName() + " scares " + target.getName() + ", giving them a heart attack." +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                }
            },
            (user, target) -> {
                if (user.equals(target)) {
                    target.dealDamage(10);
                    return target.getName() + " has an existential crisis." +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                } else {
                    target.dealDamage(random.nextInt(15, 30));
                    return user.getName() + " smacks " + target.getName() + " with a branch." +
                            (target.isDead() ? "\n" + target.getName() + " dies!" : "");
                }
            }
    );

    public static CompetitionActionRunnable getRandomAction() {
        return actions.get(random.nextInt(actions.size()));
    }
}
