package com.sosgame.view;

import com.sosgame.controller.GameController;
import com.sosgame.model.GameMode;
import com.sosgame.model.Player;
import com.sosgame.model.SOSSequence;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Enhanced Sprint 3 GUI with SOS highlighting.
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

    // no per-cell buttons; BoardPanel handles drawing and clicks

    public SOSGameGUI() {
        super("SOS Game - Sprint 3");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildBoardPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        // Start a default game so the grid and letters can render immediately
        int initialSize = 3;
        sizeSpinner.setValue(initialSize);
        controller.startNewGame(initialSize, GameMode.SIMPLE);
        rebuildBoard();
        refreshUI();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        boardPanel.repaint();
    }

    private JPanel buildTopPanel() {
        JPanel outer = new JPanel(new GridLayout(2,1));

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
        newGameBtn.addActionListener(e -> {
            int size = (int) sizeSpinner.getValue();
            GameMode mode = simpleModeBtn.isSelected() ? GameMode.SIMPLE : GameMode.GENERAL;
            controller.startNewGame(size, mode);
            rebuildBoard();
            refreshUI();
        });
        row1.add(newGameBtn);

        outer.add(row1);

        // ---- row 2: letter select + labels ----
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        placeSBtn = new JRadioButton("Place S", true);
        placeOBtn = new JRadioButton("Place O", false);
        ButtonGroup letterGroup = new ButtonGroup();
        letterGroup.add(placeSBtn);
        letterGroup.add(placeOBtn);
        row2.add(placeSBtn);
        row2.add(placeOBtn);

        turnLabel = new JLabel("Turn: -");
        row2.add(turnLabel);

        scoreLabel = new JLabel("Score:");
        row2.add(scoreLabel);

        statusLabel = new JLabel("Status:");
        row2.add(statusLabel);

        outer.add(row2);

        return outer;
    }

    private JPanel buildBoardPanel() {
        boardPanel = new BoardPanel(controller);
        // BoardPanel will always use controller's required letter for safety
        boardPanel.setOnStateChanged(this::refreshUI);
        return boardPanel;
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(new JLabel("Sprint 3 Demo UI - Click cells to place S or O"));
        return bottom;
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
        // turn label and required letter locking
        Player p = controller.getCurrentPlayer();
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
                Color color = (seq.getPlayer() == Player.BLUE) ? Color.BLUE : Color.RED;
                lines.add(new BoardPanel.HighlightLine(seq.getRow1(), seq.getCol1(), seq.getRow3(), seq.getCol3(), color));
            }
        }
        boardPanel.setHighlightLines(lines);
        boardPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SOSGameGUI::new);
    }
}