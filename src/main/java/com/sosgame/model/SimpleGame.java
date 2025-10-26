package com.sosgame.model;

/**
 * Simple mode rules:
 * - First SOS ends game immediately -> that player wins.
 * - If board fills with no SOS -> draw.
 */
public class SimpleGame extends SOSGameBase {

    private Player winner; // null means draw OR no winner yet

    public SimpleGame(int size) {
        super(size);
        this.winner = null;
    }

    @Override
    public void makeMove(int row, int col, char letter) {
        if (gameOver) return;

        boolean placed = placeLetter(row, col, letter);
        if (!placed) {
            // invalid move (already filled or out of bounds), ignore
            return;
        }

        int sosCount = countSOSFormedByMove(row, col);

        // If the player just made at least one SOS, they instantly win.
        if (sosCount > 0) {
            winner = currentPlayer;
            gameOver = true;
            return;
        }

        // If board is full and nobody formed SOS, it's a draw.
        if (isBoardFull()) {
            winner = null; // draw
            gameOver = true;
            return;
        }

        // Otherwise continue normal alternating turns.
        switchTurn();
    }

    /**
     * Returns:
     *  - BLUE or RED if someone won
     *  - null if draw OR game not finished yet
     */
    public Player getWinner() {
        return winner;
    }
}