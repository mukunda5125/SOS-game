package com.sosgame.model;

/**
 * General mode rules:
 * - Every SOS formed this turn gives 1 point.
 * - If you scored, you KEEP your turn.
 * - If you didn't score, turn switches.
 * - Game ends ONLY when board is full.
 * - Winner = higher score. Tie = draw.
 */
public class GeneralGame extends SOSGameBase {

    private int blueScore;
    private int redScore;
    private Player winner; // null means draw or not decided yet

    public GeneralGame(int size) {
        super(size);
        this.blueScore = 0;
        this.redScore = 0;
        this.winner = null;
    }

    @Override
    public void makeMove(int row, int col, char letter) {
        if (gameOver) return;

        boolean placed = placeLetter(row, col, letter);
        if (!placed) {
            // illegal move -> ignore
            return;
        }

        int sosCount = countSOSFormedByMove(row, col);

        if (sosCount > 0) {
            // award points to whoever is currentPlayer
            if (currentPlayer == Player.BLUE) {
                blueScore += sosCount;
            } else {
                redScore += sosCount;
            }
            // IMPORTANT: same player keeps turn if they scored
        } else {
            // no score -> switch turn
            switchTurn();
        }

        // Game ends ONLY when the board is full:
        if (isBoardFull()) {
            gameOver = true;
            if (blueScore > redScore) {
                winner = Player.BLUE;
            } else if (redScore > blueScore) {
                winner = Player.RED;
            } else {
                winner = null; // draw
            }
        }
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getRedScore() {
        return redScore;
    }

    /**
     * Returns:
     *  - BLUE / RED if that player wins at end of game
     *  - null if draw or not done yet
     */
    public Player getWinner() {
        return winner;
    }
}