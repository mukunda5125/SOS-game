package com.sosgame.model;

public class Cell {
    private char value; // 'S', 'O', or '\0' for empty

    public Cell() {
        this.value = '\0';
    }

    public char getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == '\0';
    }

    public void setValue(char v) {
        this.value = Character.toUpperCase(v);
    }
}