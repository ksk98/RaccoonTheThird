package com.bots.RaccoonServer.Commands.Entertainment.Competition;

public class CompetitionContestant {
    private final String name;

    private final int baseHealth = 100;
    private int health;

    public CompetitionContestant(String name) {
        this.name = name;
        this.health = baseHealth;
    }

    public void dealDamage(int damage) {
        health -= damage;

        if (health < 0) health = 0;
        else if (health > baseHealth) health = baseHealth;
    }

    public String getName() {
        return name;
    }

    public boolean isDead() {
        return health == 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}
