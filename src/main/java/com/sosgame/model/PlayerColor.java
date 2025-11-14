package com.sosgame.model;

public enum PlayerColor {
    BLUE,
    RED;

    public PlayerColor other() {
        return this == BLUE ? RED : BLUE;
    }

    public String getName() {
        return this == BLUE ? "Blue" : "Red";
    }
}

