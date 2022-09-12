package com.bots.RaccoonServer.Commands.MineSweeper;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MinesweeperGame {
    public static final int BOMBS_MIN = 10, BOMBS_MAX = 30, BOMBS_DEFAULT = 19, BOARD_SIZE = 10;
    public static final String[] ICONS = new String[]{
            "\uD83D\uDFE6", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "\uD83D\uDCA3"
    };

    public final int bombs;
    private MinesweeperField[][] matrix;
    private List<MinesweeperField> emptyFields;
    private final Random random;

    public MinesweeperGame() {
        this(BOMBS_DEFAULT);
    }

    public MinesweeperGame(int bombs) {
        if (bombs < BOMBS_MIN)
            bombs = BOMBS_MIN;
        else if (bombs > BOMBS_MAX)
            bombs = BOMBS_MAX;

        this.bombs = bombs;
        this.random = new Random();

        createAndInit();
        placeBombs();
        exposeBiggestOpenArea();
    }

    private void createAndInit() {
        this.matrix = new MinesweeperField[BOARD_SIZE][BOARD_SIZE];
        this.emptyFields = new LinkedList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                matrix[i][j] = new MinesweeperField(i, j);
                emptyFields.add(matrix[i][j]);
            }
        }
    }

    private void placeBombs() {
        for (int i = 0; i < bombs; i++) {
            boolean placed = false;
            while (!placed)
                placed = placeBomb(random.nextInt(BOARD_SIZE), random.nextInt(BOARD_SIZE));
        }
    }

    private boolean placeBomb(int x, int y) {
        if (matrix[x][y].iconCode == 9)
            return false;

        matrix[x][y].iconCode = 9;
        emptyFields.remove(matrix[x][y]);
        matrix[x][y].isMapped = true;

        int[][] indexPairs = getIndexPairsForArea(x, y);

        for (int[] pair: indexPairs) {
            if (pair[0] < 0 || pair[1] < 0 || pair[0] >= BOARD_SIZE || pair[1] >= BOARD_SIZE)
                continue;
            if (matrix[pair[0]][pair[1]].iconCode != 9)
                matrix[pair[0]][pair[1]].iconCode += 1;
        }

        return true;
    }

    private void exposeBiggestOpenArea() {
        // Area consists of a group of empty fields surrounded by number fields (including those)
        List<List<MinesweeperField>> areas = new LinkedList<>();
        int currentArea = 0;

        for (MinesweeperField current: emptyFields) {
            if (current.isMapped)
                continue;

            List<MinesweeperField> queue = new LinkedList<>();
            areas.add(new LinkedList<>());
            addUnmappedNeighborsToQueue(queue, current.x, current.y);

            while (!queue.isEmpty()) {
                MinesweeperField currentFromQueue = queue.remove(0);
                addUnmappedNeighborsToQueue(queue, currentFromQueue.x, currentFromQueue.y);
                if (!currentFromQueue.isAssignedToEmptyArea) {
                    areas.get(currentArea).add(currentFromQueue);
                    currentFromQueue.isAssignedToEmptyArea = true;
                }
            }

            currentArea += 1;
        }

        List<MinesweeperField> biggest = areas.get(0);
        for (List<MinesweeperField> area: areas) {
            if (area.size() > biggest.size())
                biggest = area;
        }

        for (MinesweeperField field: biggest) {
            int[][] indexPairs = getIndexPairsForArea(field.x, field.y);

            for (int[] pair: indexPairs) {
                if (pair[0] < 0 || pair[1] < 0 || pair[0] >= BOARD_SIZE || pair[1] >= BOARD_SIZE)
                    continue;
                matrix[pair[0]][pair[1]].isExposed = true;
            }
        }
    }

    private int[][] getIndexPairsForArea(int x, int y) {
        return new int[][] {
                {x-1, y-1}, {x, y-1},   {x+1, y-1},
                {x-1, y},   {x, y},     {x+1, y},
                {x-1, y+1}, {x, y+1},   {x+1, y+1}
        };
    }

    private void addUnmappedNeighborsToQueue(List<MinesweeperField> queue, int x, int y) {
        if (matrix[x][y].iconCode != 0)
            return;
        int[][] indexPairs = getIndexPairsForArea(x, y);
        for (int[] pair: indexPairs) {
            if (pair[0] < 0 || pair[1] < 0 || pair[0] >= BOARD_SIZE || pair[1] >= BOARD_SIZE)
                continue;
            if (!matrix[pair[0]][pair[1]].isMapped && matrix[pair[0]][pair[1]].iconCode == 0) {
                queue.add(matrix[pair[0]][pair[1]]);
                matrix[pair[0]][pair[1]].isMapped = true;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Bomb count: ")
                .append(bombs)
                .append("\n");

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (matrix[i][j].isExposed) {
                    stringBuilder.append(matrix[i][j].getIcon());
                } else {
                    stringBuilder.append("||")
                            .append(matrix[i][j].getIcon())
                            .append("||");
                }
                if (j != BOARD_SIZE -1)
                    stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
