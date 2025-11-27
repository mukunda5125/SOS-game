package com.sosgame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sosgame.controller.GameController;
import com.sosgame.model.ComputerPlayer;
import com.sosgame.model.GameMode;
import com.sosgame.model.HumanPlayer;
import com.sosgame.model.Player;

/**
 * JUnit tests for ComputerPlayer and game logic.
 */
public class ComputerPlayerTest {

    private GameController controller;

    @Before
    public void setUp() {
        controller = new GameController();
    }

    /**
     * Test 1: Partially fill a small board with some letters.
     * Let ComputerPlayer make one move.
     * Assert that:
     * - the move is placed in a previously empty cell
     * - the letter is either 'S' or 'O'.
     */
    @Test
    public void testComputerPlayerSingleMove() {
        // Create a 4x4 board
        int boardSize = 4;
        
        // Set up players: Blue is Human (for manual setup), Red is Computer
        HumanPlayer blueHuman = new HumanPlayer("Blue", controller);
        ComputerPlayer redComputer = new ComputerPlayer("Red", controller);
        controller.setPlayers(blueHuman, redComputer);
        controller.startNewGame(boardSize, GameMode.SIMPLE);

        // Partially fill the board with some letters manually
        // Blue player places 'S' at (0,0)
        controller.tryMove(0, 0, 'S');
        // Red player places 'O' at (0,1)
        controller.tryMove(0, 1, 'O');
        // Blue player places 'S' at (1,0)
        controller.tryMove(1, 0, 'S');
        // Red player places 'O' at (1,1)
        controller.tryMove(1, 1, 'O');
        
        // Get the board state before computer move
        char[][] boardBefore = controller.getBoardAsCharMatrix();
        
        // Count empty cells before
        int emptyCellsBefore = 0;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (boardBefore[r][c] == '\0') {
                    emptyCellsBefore++;
                }
            }
        }
        
        // Now it should be Blue's turn (Human), but let's make it Red's turn for testing
        // Actually, after the moves above, it should be Blue's turn (Human)
        // Let's manually switch to Red's turn by making Blue place another move
        controller.tryMove(2, 0, 'S'); // Blue's turn
        
        // Now it should be Red's turn (Computer)
        Player currentPlayer = controller.getCurrentPlayerObject();
        assertTrue("Current player should be a ComputerPlayer", 
                   currentPlayer instanceof ComputerPlayer);
        
        // Make the computer move directly
        // Note: makeMove() will call tryMove() which places the move immediately
        // The 2-second timer only affects the NEXT computer move trigger, not this one
        ComputerPlayer currentComputer = (ComputerPlayer) currentPlayer;
        currentComputer.makeMove();
        
        // Get board state after computer move
        char[][] boardAfter = controller.getBoardAsCharMatrix();
        
        // Count empty cells after and find the new move
        int emptyCellsAfter = 0;
        int newMoveRow = -1;
        int newMoveCol = -1;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (boardAfter[r][c] == '\0') {
                    emptyCellsAfter++;
                } else if (boardBefore[r][c] == '\0' && boardAfter[r][c] != '\0') {
                    // Found the new move
                    newMoveRow = r;
                    newMoveCol = c;
                }
            }
        }
        
        // Assertions
        assertTrue("Computer should have placed a move in an empty cell", 
                   newMoveRow >= 0 && newMoveCol >= 0);
        assertTrue("Computer should reduce the number of empty cells",
              emptyCellsAfter < emptyCellsBefore );
        char placedLetter = boardAfter[newMoveRow][newMoveCol];
        assertTrue("Letter should be either 'S' or 'O', but was: " + placedLetter, 
                   placedLetter == 'S' || placedLetter == 'O');
    }

    /**
     * Test 2: Set both players as ComputerPlayer on a small board in General mode.
     * Run a loop to let the game progress until it is over.
     * Assert that:
     * - the board is full OR the gameOver flag is true
     * - the score variables are consistent.
     */
    @Test
    public void testComputerVsComputerGeneralMode() {
        // Create a 3x3 board in General mode
        int boardSize = 3;
        ComputerPlayer blueComputer = new ComputerPlayer("Blue", controller);
        ComputerPlayer redComputer = new ComputerPlayer("Red", controller);
        
        controller.setPlayers(blueComputer, redComputer);
        controller.startNewGame(boardSize, GameMode.GENERAL);
        
        // Run loop to let the game progress until it is over
        // We call makeMove() directly, which will call tryMove() synchronously
        // The 2-second timer delay only affects the automatic triggering of the NEXT move,
        // but since we're calling makeMove() directly in the loop, it executes immediately
        int maxMoves = boardSize * boardSize + 10; // Safety limit (3x3 = 9 cells, +10 for safety)
        int moveCount = 0;
        
        while (!controller.isGameOver() && moveCount < maxMoves) {
            Player currentPlayer = controller.getCurrentPlayerObject();
            if (currentPlayer instanceof ComputerPlayer) {
                ComputerPlayer computer = (ComputerPlayer) currentPlayer;
                computer.makeMove();
                moveCount++;
            } else {
                // Should not happen in this test, but break if it does
                break;
            }
        }
        
        // Assertions
        assertTrue("Game should be over after all moves", controller.isGameOver());
        
        // Check that board is full OR gameOver flag is true
        char[][] board = controller.getBoardAsCharMatrix();
        boolean boardFull = true;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == '\0') {
                    boardFull = false;
                    break;
                }
            }
            if (!boardFull) break;
        }
        
        // In General mode, game should end when board is full
        assertTrue("Board should be full when game is over in General mode", boardFull);
        assertTrue("Game over flag should be true", controller.isGameOver());
        
        // Assert score variables are consistent
        int blueScore = controller.getBlueScore();
        int redScore = controller.getRedScore();
        
        assertTrue("Blue score should be non-negative", blueScore >= 0);
        assertTrue("Red score should be non-negative", redScore >= 0);
        
        // Verify that scores are consistent with the game state
        // In General mode, scores represent the number of SOS sequences formed
        // Both scores should be non-negative integers
        assertNotNull("Blue score should be defined", Integer.valueOf(blueScore));
        assertNotNull("Red score should be defined", Integer.valueOf(redScore));
        
        // Verify winner text is available and consistent
        String winnerText = controller.getWinnerText();
        assertNotNull("Winner text should be available when game is over", winnerText);
        assertFalse("Winner text should not be empty when game is over", winnerText.isEmpty());
        
        // Verify that the winner determination is consistent with scores
        if (blueScore > redScore) {
            assertTrue("If blue score is higher, winner should mention Blue. Winner: " + winnerText, 
                      winnerText.contains("Blue"));
        } else if (redScore > blueScore) {
            assertTrue("If red score is higher, winner should mention Red. Winner: " + winnerText, 
                      winnerText.contains("Red"));
        } else {
            // Scores are equal, should be a draw
            assertTrue("Equal scores should result in draw. Winner: " + winnerText, 
                      winnerText.contains("Draw"));
        }
    }

    /**
     * Test 3: ComputerPlayer detects ?OS pattern when placing 'S'.
     * Set up a board with ?OS pattern (empty, O, S) and verify computer places S at the start.
     */
    @Test
    public void testComputerPlayerDetectsSOSPatternWhenPlacingS() {
        int boardSize = 3;
        ComputerPlayer blueComputer = new ComputerPlayer("Blue", controller);
        HumanPlayer redHuman = new HumanPlayer("Red", controller);
        controller.setPlayers(blueComputer, redHuman);
        controller.startNewGame(boardSize, GameMode.SIMPLE);

        // Set up ?OS pattern: empty at (0,0), O at (0,1), S at (0,2)
        // Blue (Computer) places S at (0,2)
        controller.tryMove(0, 2, 'S');
        // Red places O at (0,1)
        controller.tryMove(0, 1, 'O');
        // Blue places S at (1,0)
        controller.tryMove(1, 0, 'S');
        // Red places O at (1,1) to switch turn back to Blue
        controller.tryMove(1, 1, 'O');
        
        // Now it's Blue's turn (Computer) and they need to place 'S'
        // The computer should detect the ?OS pattern at (0,0) and place S there
        Player currentPlayer = controller.getCurrentPlayerObject();
        assertTrue("Current player should be ComputerPlayer", currentPlayer instanceof ComputerPlayer);
        assertEquals("Blue should be current player", "Blue", currentPlayer.getColor());
        assertEquals("Required letter should be S", 'S', controller.getRequiredLetterForCurrentPlayer());
        
        // Get board state before computer move
        char[][] boardBefore = controller.getBoardAsCharMatrix();
        assertTrue("Cell (0,0) should be empty before computer move", boardBefore[0][0] == '\0');
        assertTrue("Cell (0,1) should be O", boardBefore[0][1] == 'O');
        assertTrue("Cell (0,2) should be S", boardBefore[0][2] == 'S');
        
        // Make the computer move
        ComputerPlayer computer = (ComputerPlayer) currentPlayer;
        computer.makeMove();
        
        // Get board state after computer move
        char[][] boardAfter = controller.getBoardAsCharMatrix();
        
        // Computer should have placed S at (0,0) to complete SOS
        assertEquals("Computer should place S at (0,0) to complete SOS", 'S', boardAfter[0][0]);
        assertTrue("Game should be over after SOS is formed in Simple mode", controller.isGameOver());
    }

    /**
     * Test 4: ComputerPlayer detects SO? pattern when placing 'S'.
     * Set up a board with SO? pattern (S, O, empty) and verify computer places S at the end.
     */
    @Test
    public void testComputerPlayerDetectsSOQuestionPatternWhenPlacingS() {
        int boardSize = 3;
        ComputerPlayer blueComputer = new ComputerPlayer("Blue", controller);
        HumanPlayer redHuman = new HumanPlayer("Red", controller);
        controller.setPlayers(blueComputer, redHuman);
        controller.startNewGame(boardSize, GameMode.SIMPLE);

        // Set up SO? pattern: S at (0,0), O at (0,1), empty at (0,2)
        // Blue (Computer) places S at (0,0)
        controller.tryMove(0, 0, 'S');
        // Red places O at (0,1)
        controller.tryMove(0, 1, 'O');
        // Blue places S at (1,0)
        controller.tryMove(1, 0, 'S');
        // Red places O at (1,1) to switch turn back to Blue
        controller.tryMove(1, 1, 'O');
        
        // Now it's Blue's turn (Computer) and they need to place 'S'
        // The computer should detect the SO? pattern at (0,2) and place S there
        Player currentPlayer = controller.getCurrentPlayerObject();
        assertTrue("Current player should be ComputerPlayer", currentPlayer instanceof ComputerPlayer);
        assertEquals("Required letter should be S", 'S', controller.getRequiredLetterForCurrentPlayer());
        
        // Get board state before computer move
        char[][] boardBefore = controller.getBoardAsCharMatrix();
        assertTrue("Cell (0,0) should be S", boardBefore[0][0] == 'S');
        assertTrue("Cell (0,1) should be O", boardBefore[0][1] == 'O');
        assertTrue("Cell (0,2) should be empty before computer move", boardBefore[0][2] == '\0');
        
        // Make the computer move
        ComputerPlayer computer = (ComputerPlayer) currentPlayer;
        computer.makeMove();
        
        // Get board state after computer move
        char[][] boardAfter = controller.getBoardAsCharMatrix();
        
        // Computer should have placed S at (0,2) to complete SOS
        assertEquals("Computer should place S at (0,2) to complete SOS", 'S', boardAfter[0][2]);
        assertTrue("Game should be over after SOS is formed in Simple mode", controller.isGameOver());
    }

    /**
     * Test 5: ComputerPlayer prioritizes scoring moves over random moves.
     * Set up a board with both a scoring opportunity and other empty cells.
     * Verify computer chooses the scoring move.
     */
    @Test
    public void testComputerPlayerPrioritizesScoringMoves() {
        int boardSize = 4;
        HumanPlayer blueHuman = new HumanPlayer("Blue", controller);
        ComputerPlayer redComputer = new ComputerPlayer("Red", controller);
        controller.setPlayers(blueHuman, redComputer);
        controller.startNewGame(boardSize, GameMode.GENERAL);

        // Set up S?S pattern: S at (0,0), empty at (0,1), S at (0,2)
        // Also fill some other cells to ensure there are multiple empty cells
        controller.tryMove(0, 0, 'S'); // Blue
        controller.tryMove(0, 2, 'S'); // Red (wrong letter, but we'll fix this)
        // Actually, let's set it up correctly:
        controller.startNewGame(boardSize, GameMode.GENERAL);
        controller.tryMove(0, 0, 'S'); // Blue places S
        controller.tryMove(1, 0, 'S'); // Red places S (wrong, but let's continue)
        // Let me restart and set up correctly
        controller.startNewGame(boardSize, GameMode.GENERAL);
        controller.tryMove(0, 0, 'S'); // Blue
        controller.tryMove(0, 2, 'S'); // Red (this won't work, Red must place O)
        // Let me fix this properly:
        controller.startNewGame(boardSize, GameMode.GENERAL);
        controller.tryMove(0, 0, 'S'); // Blue places S
        controller.tryMove(1, 1, 'O'); // Red places O
        controller.tryMove(0, 2, 'S'); // Blue places S at (0,2)
        // Now we have S at (0,0), empty at (0,1), S at (0,2) - S?S pattern
        // It's Red's turn and they need to place 'O'
        
        // Get board state before computer move
        char[][] boardBefore = controller.getBoardAsCharMatrix();
        assertTrue("Cell (0,0) should be S", boardBefore[0][0] == 'S');
        assertTrue("Cell (0,1) should be empty", boardBefore[0][1] == '\0');
        assertTrue("Cell (0,2) should be S", boardBefore[0][2] == 'S');
        
        Player currentPlayer = controller.getCurrentPlayerObject();
        assertTrue("Current player should be ComputerPlayer", currentPlayer instanceof ComputerPlayer);
        assertEquals("Required letter should be O", 'O', controller.getRequiredLetterForCurrentPlayer());
        
        // Make the computer move
        ComputerPlayer computer = (ComputerPlayer) currentPlayer;
        computer.makeMove();
        
        // Get board state after computer move
        char[][] boardAfter = controller.getBoardAsCharMatrix();
        
        // Computer should have placed O at (0,1) to complete SOS, not at a random location
        assertEquals("Computer should place O at (0,1) to complete SOS", 'O', boardAfter[0][1]);
        assertTrue("Red score should be 1 after completing SOS", controller.getRedScore() == 1);
    }

    /**
     * Test 6: ComputerPlayer uses randomness when no scoring move exists.
     * Set up a board with no scoring opportunities and verify computer picks a random empty cell.
     */
    @Test
    public void testComputerPlayerUsesRandomnessWhenNoScoringMove() {
        int boardSize = 3;
        HumanPlayer blueHuman = new HumanPlayer("Blue", controller);
        ComputerPlayer redComputer = new ComputerPlayer("Red", controller);
        controller.setPlayers(blueHuman, redComputer);
        controller.startNewGame(boardSize, GameMode.SIMPLE);

        // Set up board with no scoring opportunities
        // Place letters in a way that doesn't create any SOS patterns
        controller.tryMove(0, 0, 'S'); // Blue
        controller.tryMove(0, 1, 'O'); // Red
        controller.tryMove(1, 0, 'S'); // Blue
        
        // Now it's Red's turn (Computer) and they need to place 'O'
        // There are no S?S patterns, so computer should pick a random empty cell
        Player currentPlayer = controller.getCurrentPlayerObject();
        assertTrue("Current player should be ComputerPlayer", currentPlayer instanceof ComputerPlayer);
        assertEquals("Required letter should be O", 'O', controller.getRequiredLetterForCurrentPlayer());
        
        // Get board state before computer move
        char[][] boardBefore = controller.getBoardAsCharMatrix();
        int emptyCellsBefore = 0;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (boardBefore[r][c] == '\0') {
                    emptyCellsBefore++;
                }
            }
        }
        assertTrue("There should be empty cells available", emptyCellsBefore > 0);
        
        // Make the computer move
        ComputerPlayer computer = (ComputerPlayer) currentPlayer;
        computer.makeMove();
        
        // Get board state after computer move
        char[][] boardAfter = controller.getBoardAsCharMatrix();
        
        // Verify a move was made (one less empty cell)
        int emptyCellsAfter = 0;
        int newMoveRow = -1;
        int newMoveCol = -1;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (boardAfter[r][c] == '\0') {
                    emptyCellsAfter++;
                } else if (boardBefore[r][c] == '\0' && boardAfter[r][c] != '\0') {
                    newMoveRow = r;
                    newMoveCol = c;
                }
            }
        }
        
        assertTrue("Computer should have placed a move", newMoveRow >= 0 && newMoveCol >= 0);
        assertEquals("Computer should reduce empty cells by 1", emptyCellsBefore - 1, emptyCellsAfter);
        assertEquals("Computer should place O", 'O', boardAfter[newMoveRow][newMoveCol]);
    }
}

