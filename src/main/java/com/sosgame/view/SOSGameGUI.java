package com.sosgame.view;

import com.sosgame.controller.GameController;
import com.sosgame.model.ComputerPlayer;
import com.sosgame.model.GameMode;
import com.sosgame.model.GameRecord;
import com.sosgame.model.HumanPlayer;
import com.sosgame.model.Player;
import com.sosgame.model.PlayerColor;
import com.sosgame.model.SOSSequence;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Enhanced Sprint 4 GUI with player type selection and SOS highlighting.
 */
public class SOSGameGUI extends JFrame {

    private final GameController controller = new GameController();

    private BoardPanel boardPanel;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private JLabel statusLabel;

    private JSpinner sizeSpinner;
    private JRadioButton simpleModeBtn;
    private JRadioButton generalModeBtn;
    private JRadioButton placeSBtn;
    private JRadioButton placeOBtn;
    
    // Player type selection
    private JRadioButton blueHumanBtn;
    private JRadioButton blueComputerBtn;
    private JRadioButton redHumanBtn;
    private JRadioButton redComputerBtn;
    
    // Recording and replay controls
    private JToggleButton recordBtn;
    private JButton replayBtn;
    private boolean recordingSaved = false;

    // no per-cell buttons; BoardPanel handles drawing and clicks

    public SOSGameGUI() {
        super("SOS Game - Sprint 4");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Register callback for game state changes (for computer moves and replay)
        controller.setOnStateChanged(this::refreshUI);

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildBoardPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        // Start a default game so the grid and letters can render immediately
        int initialSize = 3;
        sizeSpinner.setValue(initialSize);
        startNewGameWithSettings();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        boardPanel.repaint();
    }

    private JPanel buildTopPanel() {
        JPanel outer = new JPanel(new GridLayout(3, 1));

        // ---- row 1: settings/new game ----
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        row1.add(new JLabel("Board size:"));
        sizeSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 10, 1));
        row1.add(sizeSpinner);

        simpleModeBtn = new JRadioButton("Simple", true);
        generalModeBtn = new JRadioButton("General", false);
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(simpleModeBtn);
        modeGroup.add(generalModeBtn);
        row1.add(simpleModeBtn);
        row1.add(generalModeBtn);

        JButton newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(e -> startNewGameWithSettings());
        row1.add(newGameBtn);
        
        recordBtn = new JToggleButton("Record game");
        row1.add(recordBtn);
        
        replayBtn = new JButton("Replay");
        replayBtn.addActionListener(e -> handleReplay());
        row1.add(replayBtn);

        outer.add(row1);

        // ---- row 2: player type selection ----
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        row2.add(new JLabel("Blue:"));
        blueHumanBtn = new JRadioButton("Human", true);
        blueComputerBtn = new JRadioButton("Computer", false);
        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueHumanBtn);
        blueGroup.add(blueComputerBtn);
        row2.add(blueHumanBtn);
        row2.add(blueComputerBtn);

        row2.add(new JLabel("  Red:"));
        redHumanBtn = new JRadioButton("Human", true);
        redComputerBtn = new JRadioButton("Computer", false);
        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redHumanBtn);
        redGroup.add(redComputerBtn);
        row2.add(redHumanBtn);
        row2.add(redComputerBtn);

        outer.add(row2);

        // ---- row 3: letter select + labels ----
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        placeSBtn = new JRadioButton("Place S", true);
        placeOBtn = new JRadioButton("Place O", false);
        ButtonGroup letterGroup = new ButtonGroup();
        letterGroup.add(placeSBtn);
        letterGroup.add(placeOBtn);
        row3.add(placeSBtn);
        row3.add(placeOBtn);

        turnLabel = new JLabel("Turn: -");
        row3.add(turnLabel);

        scoreLabel = new JLabel("Score:");
        row3.add(scoreLabel);

        statusLabel = new JLabel("Status:");
        row3.add(statusLabel);

        outer.add(row3);

        return outer;
    }

    private JPanel buildBoardPanel() {
        boardPanel = new BoardPanel(controller);
        // BoardPanel will get selected letter from GUI controls
        boardPanel.setOnStateChanged(this::refreshUI);
        boardPanel.setGetSelectedLetter(() -> {
            // Get the selected letter S or O from GUI controls
            return placeSBtn.isSelected() ? 'S' : 'O';
        });
        return boardPanel;
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(new JLabel("Sprint 4 - Select player types and click New Game"));
        return bottom;
    }
    
    /**
     * Start a new game with the current settings from the GUI.
     * Reads board size, game mode, and player types, then creates
     * appropriate Player objects and starts the game.
     */
    private void startNewGameWithSettings() {
        // Read board size
        int size = (int) sizeSpinner.getValue();
        
        // Read game mode
        GameMode mode = simpleModeBtn.isSelected() ? GameMode.SIMPLE : GameMode.GENERAL;
        
        // Read player types and create Player objects
        boolean blueIsComputer = blueComputerBtn.isSelected();
        boolean redIsComputer = redComputerBtn.isSelected();
        
        Player bluePlayer;
        if (blueHumanBtn.isSelected()) {
            bluePlayer = new HumanPlayer("Blue", controller);
        } else {
            bluePlayer = new ComputerPlayer("Blue", controller);
        }
        
        Player redPlayer;
        if (redHumanBtn.isSelected()) {
            redPlayer = new HumanPlayer("Red", controller);
        } else {
            redPlayer = new ComputerPlayer("Red", controller);
        }
        
        // Set players on the controller
        controller.setPlayers(bluePlayer, redPlayer);
        
        // Reset recording saved flag
        recordingSaved = false;
        
        // Start recording if record button is selected
        if (recordBtn.isSelected()) {
            controller.startRecording(mode, size, blueIsComputer, redIsComputer);
        }
        
        // Start the game (this will trigger computer moves if first player is computer)
        controller.startNewGame(size, mode);
        
        // Update the board display
        rebuildBoard();
        refreshUI();
    }
    
    /**
     * Handle replay button click.
     * Opens a file chooser to select a recording, loads it, and starts replay.
     */
    private void handleReplay() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Recording File");
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                controller.loadRecordingFromFile(file);
                
                // Get the loaded record to set up the game
                GameRecord record = controller.getReplayRecord();
                if (record == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to load recording", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Set up players based on recording
                Player bluePlayer = record.isBlueIsComputer() 
                    ? new ComputerPlayer("Blue", controller)
                    : new HumanPlayer("Blue", controller);
                Player redPlayer = record.isRedIsComputer()
                    ? new ComputerPlayer("Red", controller)
                    : new HumanPlayer("Red", controller);
                controller.setPlayers(bluePlayer, redPlayer);
                
                // Start a new game with the recording's settings
                controller.startNewGame(record.getBoardSize(), record.getMode());
                rebuildBoard();
                
                // Disable UI interactions during replay
                setUIEnabled(false);
                
                // Start replay
                controller.startReplay(() -> {
                    // Replay finished - re-enable UI
                    setUIEnabled(true);
                    refreshUI();
                });
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading recording: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Enable or disable UI interactions.
     * @param enabled true to enable, false to disable
     */
    private void setUIEnabled(boolean enabled) {
        recordBtn.setEnabled(enabled);
        replayBtn.setEnabled(enabled);
        sizeSpinner.setEnabled(enabled);
        simpleModeBtn.setEnabled(enabled);
        generalModeBtn.setEnabled(enabled);
        blueHumanBtn.setEnabled(enabled);
        blueComputerBtn.setEnabled(enabled);
        redHumanBtn.setEnabled(enabled);
        redComputerBtn.setEnabled(enabled);
        placeSBtn.setEnabled(enabled && !controller.isReplayMode());
        placeOBtn.setEnabled(enabled && !controller.isReplayMode());
    }

    // Board drawing logic moved into standalone BoardPanel class

    /**
     * Build the clickable grid based on the game size from controller.
     * Call this every time we start a new game.
     */
    private void rebuildBoard() {
        // board size used implicitly for painting; no per-cell widgets to update here
        boardPanel.setBoardSize(controller.getBoardSize());
        pack();
        boardPanel.repaint();
    }

    /**
     * Update board buttons, turn label, score, and game status.
     * Call this after any move.
     */
    private void refreshUI() {
        // Check if game ended and recording is enabled - prompt to save (only once)
        if (controller.isGameOver() && recordBtn.isSelected() && !recordingSaved) {
            recordingSaved = true;
            // Save recording when game ends
            saveRecording();
        }
        // turn label and required letter locking
        PlayerColor p = controller.getCurrentPlayer();
        char required = controller.getRequiredLetterForCurrentPlayer();
        turnLabel.setText("Turn: " + (p == null ? "-" : p.getName()));
        placeSBtn.setSelected(required == 'S');
        placeOBtn.setSelected(required == 'O');
        placeSBtn.setEnabled(required == 'S');
        placeOBtn.setEnabled(required == 'O');

        // score label
        if (controller.isGeneralMode()) {
            scoreLabel.setText("Score: Blue " + controller.getBlueScore() +
                               " - Red " + controller.getRedScore());
        } else {
            scoreLabel.setText("Score: (Simple mode)");
        }

        // status label
        if (controller.isGameOver()) {
            statusLabel.setText("Status: " + controller.getWinnerText());
        } else {
            statusLabel.setText("Turn: " + (p == null ? "-" : p.getName()) + " — must place " + required);
        }

        // Build highlight lines and send to BoardPanel
        List<SOSSequence> sequences = controller.getAllSOSSequences();
        java.util.ArrayList<BoardPanel.HighlightLine> lines = new java.util.ArrayList<>();
        if (sequences != null) {
            for (SOSSequence seq : sequences) {
                Color color = (seq.getPlayer() == PlayerColor.BLUE) ? Color.BLUE : Color.RED;
                lines.add(new BoardPanel.HighlightLine(seq.getRow1(), seq.getCol1(), seq.getRow3(), seq.getCol3(), color));
            }
        }
        boardPanel.setHighlightLines(lines);
        boardPanel.repaint();
    }
    
    /**
     * Save the current recording to a file.
     * Opens a file chooser and saves the recording.
     */
    private void saveRecording() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Recording");
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                controller.saveRecordingToFile(file);
                JOptionPane.showMessageDialog(this, 
                    "Recording saved successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving recording: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SOSGameGUI::new);
    }
}