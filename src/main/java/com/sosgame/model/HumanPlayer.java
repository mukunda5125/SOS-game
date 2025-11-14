package com.sosgame.model;

import com.sosgame.controller.GameController;

/**
 * Represents a human player in the SOS game.
 * The makeMove() method does not directly play a move since
 * the actual move comes from clicking a cell in the GUI.
 */
public class HumanPlayer extends Player {

    /**
     * Constructor for HumanPlayer.
     * @param color The color of the player ("Blue" or "Red")
     * @param game The game controller that manages the board and rules
     */
    public HumanPlayer(String color, GameController game) {
        super(color, game);
    }

    /**
     * For human players, makeMove() does not directly play a move.
     * The actual move comes from clicking a cell in the GUI.
     * This method can be used to update the GUI status if needed.
     */
    @Override
    public void makeMove() {
        // Human moves come from GUI clicks, so this method is intentionally simple.
        // The GUI will handle displaying "Current turn: Blue (click a cell)" etc.
    }
}

