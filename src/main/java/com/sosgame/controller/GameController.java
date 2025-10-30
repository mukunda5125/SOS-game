package com.sosgame.controller;

import com.sosgame.model.GameMode;
import com.sosgame.model.GeneralGame;
import com.sosgame.model.Player;
import com.sosgame.model.SimpleGame;
import com.sosgame.model.SOSGameBase;
import com.sosgame.model.SOSSequence;

import java.util.List;

public class GameController {

    private SOSGameBase game;
    private GameMode mode;
    private final char blueLetter = 'S';
    private final char redLetter  = 'O';

    public void startNewGame(int size, GameMode mode) {
        this.mode = mode;

        if (mode == GameMode.SIMPLE) {
            game = new SimpleGame(size);
        } else {
            game = new GeneralGame(size);
        }
    }

    private char letterFor(com.sosgame.model.Player p) {
        return (p == com.sosgame.model.Player.BLUE) ? blueLetter : redLetter;
    }

    /**
     * Attempt a move. Enforces per-player fixed letter.
     * Returns true if move accepted; false if rejected (no state change).
     */
    public boolean tryMove(int row, int col, char letter) {
        if (game == null) return false;
        if (game.isGameOver()) return false;

        char required = letterFor(getCurrentPlayer());
        if (letter != required) {
            return false; // reject wrong letter; keep state unchanged
        }

        game.makeMove(row, col, letter);
        return true;
    }

    // used by GUI to build the board
    public int getBoardSize() {
        return game.getBoard().getSize();
    }

    // used by GUI to paint X/Y text on each cell
    public char getCellValue(int row, int col) {
        return game.getBoard().getCell(row, col);
    }

    public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    public boolean isGameOver() {
        return game.isGameOver();
    }

    public boolean isGeneralMode() {
        return mode == GameMode.GENERAL;
    }

    public int getBlueScore() {
        if (mode == GameMode.GENERAL) {
            return ((GeneralGame) game).getBlueScore();
        }
        return 0;
    }

    public int getRedScore() {
        if (mode == GameMode.GENERAL) {
            return ((GeneralGame) game).getRedScore();
        }
        return 0;
    }

    public String getWinnerText() {
        if (!game.isGameOver()) return "";

        if (mode == GameMode.SIMPLE) {
            SimpleGame sg = (SimpleGame) game;
            Player w = sg.getWinner();
            return winnerToText(w);
        } else {
            GeneralGame gg = (GeneralGame) game;
            Player w = gg.getWinner();
            return winnerToText(w);
        }
    }

    private String winnerToText(Player w) {
        if (w == null) {
            return "Draw";
        }
        return w.getName() + " wins";
    }

    /**
     * Get all SOS sequences that have been formed in the current game.
     * Used by the GUI to draw highlight lines.
     */
    public List<SOSSequence> getAllSOSSequences() {
        if (game == null) return List.of();
        
        if (mode == GameMode.SIMPLE) {
            return ((SimpleGame) game).getAllSOSSequences();
        } else {
            return ((GeneralGame) game).getAllSOSSequences();
        }
    }

    public char getRequiredLetterForCurrentPlayer() {
        return letterFor(getCurrentPlayer());
    }

    /** Convenience method for tests/UI: snapshot of the board. */
    public char[][] getBoardAsCharMatrix() {
        int size = getBoardSize();
        char[][] m = new char[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                m[r][c] = getCellValue(r, c);
            }
        }
        return m;
    }
}