package com.sosgame.view;

import com.sosgame.controller.GameController;
import com.sosgame.model.HumanPlayer;
import com.sosgame.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * BoardPanel is responsible for rendering the SOS board grid, letters, and
 * any SOS highlight lines. It supports dynamic board sizes and never paints
 * a black screen because it clears via super.paintComponent(g).
 */
public class BoardPanel extends JPanel {

    /**
     * Simple value object to represent a highlight line between two cells.
     */
    public static class HighlightLine {
        public final int r1, c1, r2, c2;
        public final Color color;
        public HighlightLine(int r1, int c1, int r2, int c2, Color color) {
            this.r1 = r1;
            this.c1 = c1;
            this.r2 = r2;
            this.c2 = c2;
            this.color = color;
        }
    }

    private final GameController controller;
    private int boardSize;
    private final List<HighlightLine> highlightLines;

    // Callback to notify outer UI to refresh labels/status after a click
    private Runnable onStateChanged;
    
    // Callback to get the selected letter from GUI controls (S or O)
    private java.util.function.Supplier<Character> getSelectedLetter;

    public BoardPanel(GameController controller) {
        this.controller = controller;
        this.boardSize = 3;
        this.highlightLines = new ArrayList<>();

        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e);
            }
        });
    }

    public void setBoardSize(int newSize) {
        this.boardSize = Math.max(3, newSize);
        repaint();
    }

    public void setOnStateChanged(Runnable onStateChanged) {
        this.onStateChanged = onStateChanged;
    }
    
    /**
     * Set a callback to get the selected letter (S or O) from GUI controls.
     * @param getSelectedLetter Supplier that returns the selected letter ('S' or 'O')
     */
    public void setGetSelectedLetter(java.util.function.Supplier<Character> getSelectedLetter) {
        this.getSelectedLetter = getSelectedLetter;
    }

    /** Replace all highlight lines and repaint. */
    public void setHighlightLines(List<HighlightLine> lines) {
        this.highlightLines.clear();
        if (lines != null) {
            this.highlightLines.addAll(lines);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // prevent black screen and clear properly
        setBackground(Color.WHITE);

        if (boardSize <= 0) return;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int cellW = w / boardSize;
            int cellH = h / boardSize;

            // draw grid
            g2.setColor(Color.LIGHT_GRAY);
            for (int r = 0; r <= boardSize; r++) {
                int y = r * cellH;
                g2.drawLine(0, y, cellW * boardSize, y);
            }
            for (int c = 0; c <= boardSize; c++) {
                int x = c * cellW;
                g2.drawLine(x, 0, x, cellH * boardSize);
            }

            // draw letters centered in each cell
            g2.setColor(Color.BLACK);
            Font base = getFont();
            int fontSize = Math.min(cellW, cellH) - 16;
            fontSize = Math.max(fontSize, 12);
            g2.setFont(base.deriveFont(Font.BOLD, (float) fontSize));
            FontMetrics fm = g2.getFontMetrics();

            for (int r = 0; r < boardSize; r++) {
                for (int c = 0; c < boardSize; c++) {
                    char val = controller.getCellValue(r, c);
                    if (val == '\0') continue;

                    String s = Character.toString(val);
                    int x = c * cellW;
                    int y = r * cellH;
                    int textW = fm.stringWidth(s);
                    int textH = fm.getAscent();
                    int cx = x + (cellW - textW) / 2;
                    int cy = y + (cellH + textH) / 2 - 4;
                    g2.drawString(s, cx, cy);
                }
            }

            // draw highlight lines on top
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (HighlightLine hl : highlightLines) {
                g2.setColor(hl.color);
                int x1 = hl.c1 * cellW + cellW / 2;
                int y1 = hl.r1 * cellH + cellH / 2;
                int x2 = hl.c2 * cellW + cellW / 2;
                int y2 = hl.r2 * cellH + cellH / 2;
                g2.drawLine(x1, y1, x2, y2);
            }
        } finally {
            g2.dispose();
        }
    }

    private void handleClick(MouseEvent e) {
        if (controller.isGameOver()) return;
        if (boardSize <= 0) return;
        
        // Ignore clicks during replay mode
        if (controller.isReplayMode()) return;

        // Check if currentPlayer is a HumanPlayer
        Player currentPlayer = controller.getCurrentPlayerObject();
        if (currentPlayer == null || !(currentPlayer instanceof HumanPlayer)) {
            // If currentPlayer is NOT a HumanPlayer (i.e., it's ComputerPlayer), do nothing on click
            return;
        }

        int w = getWidth();
        int h = getHeight();
        int cellW = w / boardSize;
        int cellH = h / boardSize;

        int col = e.getX() / cellW;
        int row = e.getY() / cellH;

        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) return;

        // Get the selected letter S or O for that player from the GUI controls
        char letter;
        if (getSelectedLetter != null) {
            letter = getSelectedLetter.get();
        } else {
            // Fallback: use controller's required letter if callback not set
            letter = controller.getRequiredLetterForCurrentPlayer();
        }

        // Call the game controller to place the letter at that row, col
        // The game controller will:
        // - validate the move
        // - update the board and GUI
        // - check for SOS / winner
        // - switch currentPlayer
        // - trigger computer moves if the next player is ComputerPlayer
        boolean ok = controller.tryMove(row, col, letter);
        if (!ok) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Invalid move. It's " + (controller.getCurrentPlayer() == null ? "-" : controller.getCurrentPlayer().getName()) +
                "'s turn. Required letter: " + controller.getRequiredLetterForCurrentPlayer());
        }

        if (onStateChanged != null) {
            onStateChanged.run();
        }

        repaint();
    }
}


