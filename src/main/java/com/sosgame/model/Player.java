package com.sosgame.model;

import com.sosgame.controller.GameController;

/**
 * Abstract base class for players in the SOS game.
 * Represents a player that can make moves in the game.
 */
public abstract class Player {
    protected String color; // "Blue" or "Red"
    protected GameController game;

    /**
     * Constructor for Player.
     * @param color The color of the player ("Blue" or "Red")
     * @param game The game controller that manages the board and rules
     */
    public Player(String color, GameController game) {
        this.color = color;
        this.game = game;
    }

    /**
     * Get the color of this player.
     * @return The color string ("Blue" or "Red")
     */
    public String getColor() {
        return color;
    }

    /**
     * Abstract method to make a move.
     * Subclasses must implement this to define how the player makes moves.
     */
    public abstract void makeMove();
}
