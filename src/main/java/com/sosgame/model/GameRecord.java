package com.sosgame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete game recording.
 * Stores game settings and all moves made during the game.
 */
public class GameRecord {
    private GameMode mode;
    private int boardSize;
    private boolean blueIsComputer;
    private boolean redIsComputer;
    private List<MoveRecord> moves;

    public GameRecord() {
        this.moves = new ArrayList<>();
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public boolean isBlueIsComputer() {
        return blueIsComputer;
    }

    public void setBlueIsComputer(boolean blueIsComputer) {
        this.blueIsComputer = blueIsComputer;
    }

    public boolean isRedIsComputer() {
        return redIsComputer;
    }

    public void setRedIsComputer(boolean redIsComputer) {
        this.redIsComputer = redIsComputer;
    }

    public List<MoveRecord> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveRecord> moves) {
        this.moves = moves;
    }

    /**
     * Add a move to the recording.
     * @param move The move to add
     */
    public void addMove(MoveRecord move) {
        if (moves == null) {
            moves = new ArrayList<>();
        }
        moves.add(move);
    }
}

