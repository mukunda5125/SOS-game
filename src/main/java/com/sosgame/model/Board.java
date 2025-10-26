package com.sosgame.model;

public class Board {
    private final int size;
    private final Cell[][] grid;

    public Board(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Board must be at least 3x3");
        }
        this.size = size;
        this.grid = new Cell[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = new Cell();
            }
        }
    }

    public int getSize() {
        return size;
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public boolean isCellEmpty(int row, int col) {
        if (!inBounds(row, col)) return false;
        return grid[row][col].isEmpty();
    }

    // returns true if move placed on an empty, valid cell.
    public boolean placeLetter(int row, int col, char letter) {
        if (!inBounds(row, col)) return false;
        if (!isValidLetter(letter)) return false;
        if (!grid[row][col].isEmpty()) return false;

        grid[row][col].setValue(letter);
        return true;
    }

    public char getCell(int row, int col) {
        if (!inBounds(row, col)) return '\0';
        return grid[row][col].getValue();
    }

    public boolean isFull() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidLetter(char letter) {
        char up = Character.toUpperCase(letter);
        return up == 'S' || up == 'O';
    }
}