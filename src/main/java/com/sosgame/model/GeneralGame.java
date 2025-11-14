package com.sosgame.model;

import java.util.ArrayList;
import java.util.List;

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
    private PlayerColor winner; // null means draw or not decided yet
    private List<SOSSequence> sosSequences; // Track all SOS sequences formed

    public GeneralGame(int size) {
        super(size);
        this.blueScore = 0;
        this.redScore = 0;
        this.winner = null;
        this.sosSequences = new ArrayList<>();
    }

    @Override
    public void makeMove(int row, int col, char letter) {
        if (gameOver) return;

        boolean placed = placeLetter(row, col, letter);
        if (!placed) {
            // illegal move -> ignore
            return;
        }

        // Get SOS sequences formed by this move
        List<SOSSequence> newSequences = getSOSSequencesFormedByMove(row, col);
        sosSequences.addAll(newSequences);

        if (!newSequences.isEmpty()) {
            // award points to whoever is currentPlayer
            if (currentPlayer == PlayerColor.BLUE) {
                blueScore += newSequences.size();
            } else {
                redScore += newSequences.size();
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
                winner = PlayerColor.BLUE;
            } else if (redScore > blueScore) {
                winner = PlayerColor.RED;
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
    public PlayerColor getWinner() {
        return winner;
    }

    /**
     * Get all SOS sequences that have been formed in the game.
     */
    public List<SOSSequence> getAllSOSSequences() {
        return new ArrayList<>(sosSequences);
    }
}