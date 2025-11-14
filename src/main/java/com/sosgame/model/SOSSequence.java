package com.sosgame.model;

/**
 * Represents a detected SOS sequence with the three cell coordinates
 * and the player who created it.
 */
public class SOSSequence {
    private final int row1, col1, row2, col2, row3, col3;
    private final PlayerColor player;
    
    public SOSSequence(int row1, int col1, int row2, int col2, int row3, int col3, PlayerColor player) {
        this.row1 = row1;
        this.col1 = col1;
        this.row2 = row2;
        this.col2 = col2;
        this.row3 = row3;
        this.col3 = col3;
        this.player = player;
    }
    
    public int getRow1() { return row1; }
    public int getCol1() { return col1; }
    public int getRow2() { return row2; }
    public int getCol2() { return col2; }
    public int getRow3() { return row3; }
    public int getCol3() { return col3; }
    public PlayerColor getPlayer() { return player; }
}
