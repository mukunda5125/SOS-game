package com.sosgame.controller;

import com.sosgame.model.ComputerPlayer;
import com.sosgame.model.GameMode;
import com.sosgame.model.GeneralGame;
import com.sosgame.model.GameRecord;
import com.sosgame.model.MoveRecord;
import com.sosgame.model.Player;
import com.sosgame.model.PlayerColor;
import com.sosgame.model.SimpleGame;
import com.sosgame.model.SOSGameBase;
import com.sosgame.model.SOSSequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GameController {

    private SOSGameBase game;
    private GameMode mode;
    private final char blueLetter = 'S';
    private final char redLetter  = 'O';
    
    private Player bluePlayer;
    private Player redPlayer;
    private Player currentPlayer;
    
    // Callback to notify GUI when game state changes (for computer moves)
    private Runnable onStateChanged;
    
    // Recording and replay fields
    private boolean recordingEnabled;
    private GameRecord currentRecord;
    private boolean replayMode;
    private GameRecord replayRecord;
    private int replayIndex;
    private javax.swing.Timer replayTimer;
    private Runnable onReplayFinished;

    /**
     * Set the blue and red players for the game.
     * @param blue The blue player
     * @param red The red player
     */
    public void setPlayers(Player blue, Player red) {
        this.bluePlayer = blue;
        this.redPlayer = red;
    }
    
    /**
     * Set a callback to be notified when the game state changes.
     * This is used by the GUI to refresh after computer moves.
     * @param callback The callback to run after state changes
     */
    public void setOnStateChanged(Runnable callback) {
        this.onStateChanged = callback;
    }

    public void startNewGame(int size, GameMode mode) {
        this.mode = mode;

        if (mode == GameMode.SIMPLE) {
            game = new SimpleGame(size);
        } else {
            game = new GeneralGame(size);
        }
        
        // Set currentPlayer to bluePlayer at the beginning
        if (bluePlayer != null) {
            currentPlayer = bluePlayer;
        }
        
        // If the first player is a ComputerPlayer, trigger their move
        triggerComputerMoveIfNeeded();
    }

    private char letterFor(com.sosgame.model.PlayerColor p) {
        return (p == com.sosgame.model.PlayerColor.BLUE) ? blueLetter : redLetter;
    }
    
    /**
     * Get the current player color from the game model.
     * This is used for determining which letter is required.
     */
    private PlayerColor getCurrentPlayerColor() {
        if (game == null) return PlayerColor.BLUE;
        return game.getCurrentPlayer();
    }

    /**
     * Attempt a move. Enforces per-player fixed letter.
     * Returns true if move accepted; false if rejected (no state change).
     * After a move, handles player switching and triggers computer moves if needed.
     */
    public boolean tryMove(int row, int col, char letter) {
        if (game == null) return false;
        if (game.isGameOver()) return false;

        char required = letterFor(getCurrentPlayerColor());
        if (letter != required) {
            return false; // reject wrong letter; keep state unchanged
        }

        // Record the move if recording is enabled and not in replay mode
        // We record BEFORE makeMove() so we capture the player who is making the move
        PlayerColor movePlayer = null;
        if (recordingEnabled && !replayMode) {
            movePlayer = getCurrentPlayerColor(); // Player before the move
        }
        
        // Make the move (this handles SOS detection, score update, turn switching)
        game.makeMove(row, col, letter);
        
        // Record the move after it succeeds
        if (recordingEnabled && !replayMode && movePlayer != null) {
            recordMoveIfNeeded(row, col, letter, movePlayer);
        }
        
        // After move is processed, handle player switching and game state
        if (!game.isGameOver()) {
            // Check if the turn switched in the game model
            // In Simple mode: turn always switches (unless game ended)
            // In General mode: turn only switches if player didn't score
            PlayerColor newPlayerColor = game.getCurrentPlayer();
            
            // Update currentPlayer based on the new PlayerColor from game model
            // This handles both cases: turn switched OR player kept turn (General mode after scoring)
            if (newPlayerColor == PlayerColor.BLUE && bluePlayer != null) {
                currentPlayer = bluePlayer;
            } else if (newPlayerColor == PlayerColor.RED && redPlayer != null) {
                currentPlayer = redPlayer;
            }
            
            // Notify GUI of state change (scores, turn, board state)
            // This updates the status label to show "Turn: Red — must place O"
            notifyStateChanged();
            
            // Trigger computer move if needed (after turn switching and GUI update)
            triggerComputerMoveIfNeeded();
        } else {
            // Game is over (SOS formed in Simple mode, or board full in General mode)
            // Notify GUI to show winner message
            notifyStateChanged();
        }
        
        return true;
    }
    
    /**
     * Helper method to trigger computer move if the current player is a computer.
     * Called after turn switching and GUI updates.
     * This method handles all scenarios: Human vs Computer, Computer vs Human,
     * Computer vs Computer, and General mode where computer keeps turn after scoring.
     */
    private void triggerComputerMoveIfNeeded() {
        if (game == null || game.isGameOver()) return;
        
        boolean blueIsComputer = (bluePlayer instanceof ComputerPlayer);
        boolean redIsComputer = (redPlayer instanceof ComputerPlayer);
        
        PlayerColor currentPlayerColor = game.getCurrentPlayer();
        boolean computerTurn = 
            (currentPlayerColor == PlayerColor.BLUE && blueIsComputer) ||
            (currentPlayerColor == PlayerColor.RED && redIsComputer);
        
        if (!computerTurn) return;
        
        // Determine which computer player should move
        Player computerPlayerToMove = null;
        if (currentPlayerColor == PlayerColor.BLUE && blueIsComputer) {
            computerPlayerToMove = bluePlayer;
        } else if (currentPlayerColor == PlayerColor.RED && redIsComputer) {
            computerPlayerToMove = redPlayer;
        }
        
        if (!(computerPlayerToMove instanceof ComputerPlayer)) return;
        
        // Use SwingUtilities to ensure GUI updates happen on EDT
        // This prevents infinite recursion and allows GUI to update between moves
        // invokeLater ensures the computer move happens after the current event processing
        final Player computerToMove = computerPlayerToMove;
        javax.swing.SwingUtilities.invokeLater(() -> {
            executeComputerMove(computerToMove);
        });
    }
    
    /**
     * Helper method to execute the computer move with proper checks.
     * Uses a Swing Timer to delay the move by 2 seconds, allowing the GUI to update
     * and showing the "Turn: [Player] — must place [Letter]" message before the computer moves.
     */
    private void executeComputerMove(Player computerToMove) {
        // Use Swing Timer to delay computer move by 2 seconds
        // This prevents GUI freezing (unlike Thread.sleep) and allows status update to be visible
        new javax.swing.Timer(2000, e -> {
            // Double-check conditions (game might have ended, player might have changed)
            if (game != null && !game.isGameOver()) {
                PlayerColor checkColor = game.getCurrentPlayer();
                Player checkPlayer = null;
                if (checkColor == PlayerColor.BLUE && bluePlayer instanceof ComputerPlayer) {
                    checkPlayer = bluePlayer;
                } else if (checkColor == PlayerColor.RED && redPlayer instanceof ComputerPlayer) {
                    checkPlayer = redPlayer;
                }
                
                // Verify it's still the computer's turn and the player matches
                if (checkPlayer instanceof ComputerPlayer && checkPlayer == computerToMove) {
                    // Call the computer's makeMove() method
                    // This will find S?S patterns or pick random cell, then call game.tryMove()
                    checkPlayer.makeMove();
                    // makeMove() will call tryMove() again, which will process the move,
                    // update the board, check for SOS, switch turns, and trigger next computer move if needed
                }
            }
            // Stop the timer after it fires once (non-repeating)
            ((javax.swing.Timer) e.getSource()).stop();
        }).start();
    }
    
    /**
     * Notify listeners that the game state has changed.
     */
    private void notifyStateChanged() {
        if (onStateChanged != null) {
            onStateChanged.run();
        }
    }

    // used by GUI to build the board
    public int getBoardSize() {
        return game.getBoard().getSize();
    }

    // used by GUI to paint X/Y text on each cell
    public char getCellValue(int row, int col) {
        return game.getBoard().getCell(row, col);
    }

    /**
     * Get the current player color from the game model.
     * For backward compatibility with GUI code.
     */
    public PlayerColor getCurrentPlayer() {
        return getCurrentPlayerColor();
    }
    
    /**
     * Get the current Player object.
     * @return The current Player object, or null if not set
     */
    public Player getCurrentPlayerObject() {
        return currentPlayer;
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
            PlayerColor w = sg.getWinner();
            return winnerToText(w);
        } else {
            GeneralGame gg = (GeneralGame) game;
            PlayerColor w = gg.getWinner();
            return winnerToText(w);
        }
    }

    private String winnerToText(PlayerColor w) {
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
        return letterFor(getCurrentPlayerColor());
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
    
    // ========== Recording and Replay Methods ==========
    
    /**
     * Start recording a game.
     * @param mode The game mode
     * @param size The board size
     * @param blueComputer Whether blue player is a computer
     * @param redComputer Whether red player is a computer
     */
    public void startRecording(GameMode mode, int size, boolean blueComputer, boolean redComputer) {
        currentRecord = new GameRecord();
        currentRecord.setMode(mode);
        currentRecord.setBoardSize(size);
        currentRecord.setBlueIsComputer(blueComputer);
        currentRecord.setRedIsComputer(redComputer);
        recordingEnabled = true;
    }
    
    /**
     * Record a move if recording is enabled.
     * @param row The row of the move
     * @param col The column of the move
     * @param letter The letter placed
     * @param player The player who made the move
     */
    private void recordMoveIfNeeded(int row, int col, char letter, PlayerColor player) {
        if (recordingEnabled && currentRecord != null) {
            MoveRecord move = new MoveRecord(row, col, letter, player);
            currentRecord.addMove(move);
        }
    }
    
    /**
     * Save the current recording to a file.
     * @param file The file to save to
     * @throws IOException If an I/O error occurs
     */
    public void saveRecordingToFile(File file) throws IOException {
        if (currentRecord == null) {
            throw new IllegalStateException("No recording to save");
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header line
            String modeStr = currentRecord.getMode() == GameMode.SIMPLE ? "SIMPLE" : "GENERAL";
            String blueStr = currentRecord.isBlueIsComputer() ? "COMPUTER" : "HUMAN";
            String redStr = currentRecord.isRedIsComputer() ? "COMPUTER" : "HUMAN";
            writer.write(String.format("MODE=%s;SIZE=%d;BLUE=%s;RED=%s", 
                modeStr, currentRecord.getBoardSize(), blueStr, redStr));
            writer.newLine();
            
            // Write moves
            for (MoveRecord move : currentRecord.getMoves()) {
                String playerStr = move.getPlayer() == PlayerColor.BLUE ? "BLUE" : "RED";
                writer.write(String.format("%d,%d,%c,%s", 
                    move.getRow(), move.getCol(), move.getLetter(), playerStr));
                writer.newLine();
            }
        }
    }
    
    /**
     * Get the loaded replay record (for setting up the game).
     * @return The replay record, or null if none loaded
     */
    public GameRecord getReplayRecord() {
        return replayRecord;
    }
    
    /**
     * Load a recording from a file.
     * @param file The file to load from
     * @throws IOException If an I/O error occurs
     */
    public void loadRecordingFromFile(File file) throws IOException {
        replayRecord = new GameRecord();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read header line
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("Empty file");
            }
            
            // Parse header: MODE=<mode>;SIZE=<size>;BLUE=<HUMAN|COMPUTER>;RED=<HUMAN|COMPUTER>
            String[] parts = header.split(";");
            for (String part : parts) {
                String[] keyValue = part.split("=");
                if (keyValue.length != 2) continue;
                
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                
                switch (key) {
                    case "MODE":
                        replayRecord.setMode("SIMPLE".equals(value) ? GameMode.SIMPLE : GameMode.GENERAL);
                        break;
                    case "SIZE":
                        replayRecord.setBoardSize(Integer.parseInt(value));
                        break;
                    case "BLUE":
                        replayRecord.setBlueIsComputer("COMPUTER".equals(value));
                        break;
                    case "RED":
                        replayRecord.setRedIsComputer("COMPUTER".equals(value));
                        break;
                }
            }
            
            // Read moves
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                // Parse move: row,col,letter,playerColor
                String[] moveParts = line.split(",");
                if (moveParts.length != 4) continue;
                
                int row = Integer.parseInt(moveParts[0].trim());
                int col = Integer.parseInt(moveParts[1].trim());
                char letter = moveParts[2].trim().charAt(0);
                PlayerColor player = "BLUE".equals(moveParts[3].trim()) ? PlayerColor.BLUE : PlayerColor.RED;
                
                MoveRecord move = new MoveRecord(row, col, letter, player);
                replayRecord.addMove(move);
            }
        }
        
        replayIndex = 0;
    }
    
    /**
     * Check if the game is in replay mode.
     * @return true if in replay mode, false otherwise
     */
    public boolean isReplayMode() {
        return replayMode;
    }
    
    /**
     * Start replaying a loaded recording.
     * @param onReplayFinished Callback to run when replay is finished
     */
    public void startReplay(Runnable onReplayFinished) {
        if (replayRecord == null) {
            throw new IllegalStateException("No recording loaded");
        }
        
        this.onReplayFinished = onReplayFinished;
        replayMode = true;
        replayIndex = 0;
        
        // Stop any existing replay timer
        if (replayTimer != null) {
            replayTimer.stop();
        }
        
        // Create and start replay timer
        replayTimer = new javax.swing.Timer(1000, e -> {
            if (replayIndex >= replayRecord.getMoves().size()) {
                // Replay finished
                replayTimer.stop();
                replayMode = false;
                if (this.onReplayFinished != null) {
                    this.onReplayFinished.run();
                }
                return;
            }
            
            // Apply next move
            MoveRecord move = replayRecord.getMoves().get(replayIndex);
            tryMove(move.getRow(), move.getCol(), move.getLetter());
            replayIndex++;
            
            // Notify state change
            notifyStateChanged();
        });
        
        replayTimer.start();
    }
}