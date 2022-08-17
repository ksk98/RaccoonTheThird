package com.bots.RacoonServer.Commands.MineSweeper;

public class MinesweeperField {
    public final int x, y;
    public boolean isExposed = false;
    public boolean isMapped = false, isAssignedToEmptyArea = false;
    public int iconCode = 0;

    public MinesweeperField(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getIcon() {
        return MinesweeperGame.ICONS[iconCode];
    }
}
