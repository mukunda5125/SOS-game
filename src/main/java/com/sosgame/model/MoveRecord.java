package com.sosgame.model;

/**
 * Represents a single move in a game recording.
 * Stores the row, column, letter, and player color for a move.
 */
public class MoveRecord {
    private final int row;
    private final int col;
    private final char letter;
    private final PlayerColor player;

    public MoveRecord(int row, int col, char letter, PlayerColor player) {
        this.row = row;
        this.col = col;
        this.letter = letter;
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getLetter() {
        return letter;
    }

    public PlayerColor getPlayer() {
        return player;
    }
}

