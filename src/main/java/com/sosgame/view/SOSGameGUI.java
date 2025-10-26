package com.sosgame.view;

import com.sosgame.controller.GameController;
import com.sosgame.model.GameMode;
import com.sosgame.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Minimal Sprint 3 GUI.
 */
public class SOSGameGUI extends JFrame {

    private final GameController controller = new GameController();

    private JPanel boardPanel;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private JLabel statusLabel;

    private JSpinner sizeSpinner;
    private JRadioButton simpleModeBtn;
    private JRadioButton generalModeBtn;
    private JRadioButton placeSBtn;
    private JRadioButton placeOBtn;

    private JButton[][] cellButtons;

    public SOSGameGUI() {
        super("SOS Game - Sprint 3");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildBoardPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
        // placeholder board until "New Game"
        boardPanel = new JPanel(new GridLayout(3,3));
        cellButtons = new JButton[3][3];
        return boardPanel;
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(new JLabel("Sprint 3 Demo UI"));
        return bottom;
    }

    /**
     * Build the clickable grid based on the game size from controller.
     * Call this every time we start a new game.
     */
    private void rebuildBoard() {
        int size = controller.getBoardSize();

        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(size, size));
        cellButtons = new JButton[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                final int row = r;
                final int col = c;

                JButton btn = new JButton("");
                btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

                ActionListener clickAction = e -> {
                    if (controller.isGameOver()) return;

                    char letter = placeSBtn.isSelected() ? 'S' : 'O';
                    controller.handleCellClick(row, col, letter);

                    refreshUI();
                };

                btn.addActionListener(clickAction);

                cellButtons[r][c] = btn;
                boardPanel.add(btn);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
        pack();
    }

    /**
     * Update board buttons, turn label, score, and game status.
     * Call this after any move.
     */
    private void refreshUI() {
        int size = controller.getBoardSize();

        // update each cell button
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                char val = controller.getCellValue(r, c);
                String text = (val == '\0') ? "" : Character.toString(val);
                cellButtons[r][c].setText(text);

                if (controller.isGameOver()) {
                    cellButtons[r][c].setEnabled(false);
                }
            }
        }

        // turn label
        Player p = controller.getCurrentPlayer();
        turnLabel.setText("Turn: " + (p == null ? "-" : p.getName()));

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
            statusLabel.setText("Status: in progress");
        }

        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SOSGameGUI::new);
    }
}