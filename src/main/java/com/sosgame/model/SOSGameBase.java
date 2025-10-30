package com.sosgame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared functionality:
 * - board state
 * - current player
 * - placing moves
 * - detecting SOS patterns
 * - knowing if board is full
 *
 * SimpleGame and GeneralGame extend this and add their own game-over rules.
 */
public abstract class SOSGameBase {

    protected final Board board;
    protected Player currentPlayer;
    protected boolean gameOver;

    public SOSGameBase(int size) {
        this.board = new Board(size);
        this.currentPlayer = Player.BLUE; // Blue starts
        this.gameOver = false;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isBoardFull() {
        return board.isFull();
    }

    protected void switchTurn() {
        currentPlayer = currentPlayer.other();
    }

    /**
     * Put S or O on the board for the CURRENT player.
     * Returns false if illegal (out of bounds / occupied / game over).
     */
    protected boolean placeLetter(int row, int col, char letter) {
        if (gameOver) return false;
        return board.placeLetter(row, col, letter);
    }

    /**
     * Returns how many SOS patterns were formed that include (row, col).
     * We detect "S O S" in:
     *  - vertical
     *  - horizontal
     *  - diag down-right
     *  - diag down-left
     *
     * For each direction, we check 3 possible triplets where (row,col) is:
     *   - the middle cell,
     *   - the first cell,
     *   - the last cell.
     */
    protected int countSOSFormedByMove(int row, int col) {
        return getSOSSequencesFormedByMove(row, col).size();
    }

    /**
     * Returns a list of SOS sequences formed by the move at (row, col).
     * Each sequence contains the three coordinates and the player who made it.
     */
    protected List<SOSSequence> getSOSSequencesFormedByMove(int row, int col) {
        List<SOSSequence> sequences = new ArrayList<>();

        int[][] dirs = new int[][]{
            {1, 0},   // vertical
            {0, 1},   // horizontal
            {1, 1},   // diag down-right
            {1,-1}    // diag down-left
        };

        for (int[] d : dirs) {
            int dr = d[0];
            int dc = d[1];

            // Check triplet centered at (row,col):
            if (checkTriplet(row - dr, col - dc, row, col, row + dr, col + dc)) {
                sequences.add(new SOSSequence(row - dr, col - dc, row, col, row + dr, col + dc, currentPlayer));
            }

            // Check triplet starting at (row,col):
            if (checkTriplet(row, col, row + dr, col + dc, row + 2*dr, col + 2*dc)) {
                sequences.add(new SOSSequence(row, col, row + dr, col + dc, row + 2*dr, col + 2*dc, currentPlayer));
            }

            // Check triplet ending at (row,col):
            if (checkTriplet(row - 2*dr, col - 2*dc, row - dr, col - dc, row, col)) {
                sequences.add(new SOSSequence(row - 2*dr, col - 2*dc, row - dr, col - dc, row, col, currentPlayer));
            }
        }

        return sequences;
    }

    /**
     * Return true if (r1,c1),(r2,c2),(r3,c3) is exactly S,O,S and in bounds.
     * Else return false.
     */
    private boolean checkTriplet(int r1,int c1,int r2,int c2,int r3,int c3){
        if (!board.inBounds(r1,c1) || !board.inBounds(r2,c2) || !board.inBounds(r3,c3)) {
            return false;
        }
        char a = board.getCell(r1,c1);
        char b = board.getCell(r2,c2);
        char c = board.getCell(r3,c3);
        return (a=='S' && b=='O' && c=='S');
    }

    /**
     * Get all SOS sequences that have been formed in the game.
     * This is used by the GUI to draw highlight lines.
     */
    public List<SOSSequence> getAllSOSSequences() {
        List<SOSSequence> allSequences = new ArrayList<>();
        
        // Check all possible SOS patterns on the current board
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                if (board.getCell(r, c) == 'S') {
                    // Check all directions from this S
                    int[][] dirs = new int[][]{
                        {1, 0},   // vertical
                        {0, 1},   // horizontal
                        {1, 1},   // diag down-right
                        {1,-1}    // diag down-left
                    };
                    
                    for (int[] d : dirs) {
                        int dr = d[0];
                        int dc = d[1];
                        
                        // Check if this S is the start of an SOS
                        if (checkTriplet(r, c, r + dr, c + dc, r + 2*dr, c + 2*dc)) {
                            // Find which player made this SOS by checking the board state
                            // We'll need to determine the player based on the game state
                            allSequences.add(new SOSSequence(r, c, r + dr, c + dc, r + 2*dr, c + 2*dc, Player.BLUE));
                        }
                    }
                }
            }
        }
        
        return allSequences;
    }

    /**
     * Child classes define HOW a move changes the game's state:
     *  - does game end now?
     *  - how do we track winner/score?
     *  - who plays next?
     */
    public abstract void makeMove(int row, int col, char letter);
}