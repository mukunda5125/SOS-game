package com.sosgame.controller;

import com.sosgame.model.Board;
import com.sosgame.model.GameMode;
import com.sosgame.model.GeneralGame;
import com.sosgame.model.Player;
import com.sosgame.model.SimpleGame;
import com.sosgame.model.SOSGameBase;

public class GameController {

    private SOSGameBase game;
    private GameMode mode;

    public void startNewGame(int size, GameMode mode) {
        this.mode = mode;

        if (mode == GameMode.SIMPLE) {
            game = new SimpleGame(size);
        } else {
            game = new GeneralGame(size);
        }
    }

    public void handleCellClick(int row, int col, char letter) {
        if (game == null) return;
        if (game.isGameOver()) return;
        game.makeMove(row, col, letter);
    }

    // used by GUI to build the board
    public int getBoardSize() {
        return game.getBoard().getSize();
    }

    // used by GUI to paint X/Y text on each cell
    public char getCellValue(int row, int col) {
        return game.getBoard().getCell(row, col);
    }

    public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
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
            Player w = sg.getWinner();
            return winnerToText(w);
        } else {
            GeneralGame gg = (GeneralGame) game;
            Player w = gg.getWinner();
            return winnerToText(w);
        }
    }

    private String winnerToText(Player w) {
        if (w == null) {
            return "Draw";
        }
        return w.getName() + " wins";
    }
}