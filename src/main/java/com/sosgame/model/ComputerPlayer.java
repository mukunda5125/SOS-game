package com.sosgame.model;

import com.sosgame.controller.GameController;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a computer player in the SOS game.
 * Implements makeMove() by selecting a random empty cell and
 * placing a letter (though the game controller will enforce
 * the correct letter based on the current player).
 */
public class ComputerPlayer extends Player {

    private Random random;

    /**
     * Constructor for ComputerPlayer.
     * @param color The color of the player ("Blue" or "Red")
     * @param game The game controller that manages the board and rules
     */
    public ComputerPlayer(String color, GameController game) {
        super(color, game);
        this.random = new Random();
    }

    /**
     * Makes a move by:
     * 1. First, try to detect scoring opportunities:
     *    - If placing 'O': look for S?S patterns and play 'O' in the middle
     *    - If placing 'S': look for ?OS patterns (place S at start) or SO? patterns (place S at end)
     * 2. If no scoring opportunity found, pick a random empty cell
     * 3. Use the required letter for the current player (enforced by game controller)
     * 4. Call tryMove() on the game controller
     */
    @Override
    public void makeMove() {
        if (game == null || game.isGameOver()) {
            return;
        }

        char requiredLetter = game.getRequiredLetterForCurrentPlayer();
        int boardSize = game.getBoardSize();
        
        // All possible directions: vertical, horizontal, diag down-right, diag down-left
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        
        // Strategy: Look for scoring opportunities based on required letter
        if (requiredLetter == 'O') {
            // Look for S?S patterns where ? is empty - place O in the middle
            for (int r = 0; r < boardSize; r++) {
                for (int c = 0; c < boardSize; c++) {
                    char cell = game.getCellValue(r, c);
                    if (cell == 'S') {
                        // Check all directions from this S
                        for (int[] dir : directions) {
                            int dr = dir[0];
                            int dc = dir[1];
                            
                            // Check if there's an S two steps away and empty cell in between
                            int r2 = r + dr;
                            int c2 = c + dc;
                            int r3 = r + 2 * dr;
                            int c3 = c + 2 * dc;
                            
                            if (r2 >= 0 && r2 < boardSize && c2 >= 0 && c2 < boardSize &&
                                r3 >= 0 && r3 < boardSize && c3 >= 0 && c3 < boardSize) {
                                char middle = game.getCellValue(r2, c2);
                                char end = game.getCellValue(r3, c3);
                                
                                if (middle == '\0' && end == 'S') {
                                    // Found S?S pattern, place O in the middle
                                    if (game.tryMove(r2, c2, 'O')) {
                                        return; // Move successful
                                    }
                                }
                            }
                            
                            // Also check the reverse: S at end, empty in middle, S at start
                            int r1 = r - 2 * dr;
                            int c1 = c - 2 * dc;
                            r2 = r - dr;
                            c2 = c - dc;
                            
                            if (r1 >= 0 && r1 < boardSize && c1 >= 0 && c1 < boardSize &&
                                r2 >= 0 && r2 < boardSize && c2 >= 0 && c2 < boardSize) {
                                char start = game.getCellValue(r1, c1);
                                char middle = game.getCellValue(r2, c2);
                                
                                if (start == 'S' && middle == '\0' && cell == 'S') {
                                    // Found S?S pattern, place O in the middle
                                    if (game.tryMove(r2, c2, 'O')) {
                                        return; // Move successful
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (requiredLetter == 'S') {
            // Look for ?OS patterns (empty, O, S) - place S at the start
            // and SO? patterns (S, O, empty) - place S at the end
            for (int r = 0; r < boardSize; r++) {
                for (int c = 0; c < boardSize; c++) {
                    char cell = game.getCellValue(r, c);
                    
                    // Check for ?OS pattern: empty at (r,c), O at (r+dr, c+dc), S at (r+2*dr, c+2*dc)
                    // We need to place S at (r, c) to complete SOS
                    if (cell == '\0') {
                        for (int[] dir : directions) {
                            int dr = dir[0];
                            int dc = dir[1];
                            
                            int r2 = r + dr;
                            int c2 = c + dc;
                            int r3 = r + 2 * dr;
                            int c3 = c + 2 * dc;
                            
                            if (r2 >= 0 && r2 < boardSize && c2 >= 0 && c2 < boardSize &&
                                r3 >= 0 && r3 < boardSize && c3 >= 0 && c3 < boardSize) {
                                char middle = game.getCellValue(r2, c2);
                                char end = game.getCellValue(r3, c3);
                                
                                if (middle == 'O' && end == 'S') {
                                    // Found ?OS pattern, place S at (r, c)
                                    if (game.tryMove(r, c, 'S')) {
                                        return; // Move successful
                                    }
                                }
                            }
                        }
                    }
                    
                    // Check for SO? pattern: S at (r-dr, c-dc), O at (r, c), empty at (r+dr, c+dc)
                    // We need to place S at (r+dr, c+dc) to complete SOS
                    if (cell == 'O') {
                        for (int[] dir : directions) {
                            int dr = dir[0];
                            int dc = dir[1];
                            
                            int r1 = r - dr;
                            int c1 = c - dc;
                            int r2 = r + dr;
                            int c2 = c + dc;
                            
                            if (r1 >= 0 && r1 < boardSize && c1 >= 0 && c1 < boardSize &&
                                r2 >= 0 && r2 < boardSize && c2 >= 0 && c2 < boardSize) {
                                char start = game.getCellValue(r1, c1);
                                char end = game.getCellValue(r2, c2);
                                
                                if (start == 'S' && end == '\0') {
                                    // Found SO? pattern, place S at (r2, c2)
                                    if (game.tryMove(r2, c2, 'S')) {
                                        return; // Move successful
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // No scoring opportunity found, pick a random empty cell
        List<int[]> emptyCells = new ArrayList<>();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                char cellValue = game.getCellValue(row, col);
                if (cellValue == '\0') { // Empty cell
                    emptyCells.add(new int[]{row, col});
                }
            }
        }

        // If no empty cells, can't make a move
        if (emptyCells.isEmpty()) {
            return;
        }

        // Pick a random empty cell
        int[] chosenCell = emptyCells.get(random.nextInt(emptyCells.size()));
        int row = chosenCell[0];
        int col = chosenCell[1];

        // Use the required letter (game controller will enforce this anyway)
        char letter = requiredLetter;

        // Attempt the move (game controller will enforce the correct letter)
        game.tryMove(row, col, letter);
    }
}

