package com.sosgame.model;

public enum Player {
    BLUE,
    RED;

    public Player other() {
        return this == BLUE ? RED : BLUE;
    }

    public String getName() {
        return this == BLUE ? "Blue" : "Red";
    }
}