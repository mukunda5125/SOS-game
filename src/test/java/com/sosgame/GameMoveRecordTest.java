package com.sosgame;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sosgame.model.MoveRecord;
import com.sosgame.model.PlayerColor;

/**
 * JUnit tests for MoveRecord class.
 * Tests constructor and getters.
 */
public class GameMoveRecordTest {

    @Test
    public void testConstructor() {
        MoveRecord move = new MoveRecord(0, 1, 'S', PlayerColor.BLUE);
        assertNotNull("MoveRecord should not be null", move);
    }

    @Test
    public void testGetters() {
        int row = 2;
        int col = 3;
        char letter = 'O';
        PlayerColor player = PlayerColor.RED;
        
        MoveRecord move = new MoveRecord(row, col, letter, player);
        
        assertEquals("Row should match", row, move.getRow());
        assertEquals("Col should match", col, move.getCol());
        assertEquals("Letter should match", letter, move.getLetter());
        assertEquals("Player should match", player, move.getPlayer());
    }

    @Test
    public void testGettersWithDifferentValues() {
        // Test with Blue player and S
        MoveRecord move1 = new MoveRecord(0, 0, 'S', PlayerColor.BLUE);
        assertEquals("Row should be 0", 0, move1.getRow());
        assertEquals("Col should be 0", 0, move1.getCol());
        assertEquals("Letter should be S", 'S', move1.getLetter());
        assertEquals("Player should be BLUE", PlayerColor.BLUE, move1.getPlayer());
        
        // Test with Red player and O
        MoveRecord move2 = new MoveRecord(1, 2, 'O', PlayerColor.RED);
        assertEquals("Row should be 1", 1, move2.getRow());
        assertEquals("Col should be 2", 2, move2.getCol());
        assertEquals("Letter should be O", 'O', move2.getLetter());
        assertEquals("Player should be RED", PlayerColor.RED, move2.getPlayer());
    }

    @Test
    public void testMultipleMoveRecords() {
        MoveRecord move1 = new MoveRecord(0, 0, 'S', PlayerColor.BLUE);
        MoveRecord move2 = new MoveRecord(0, 1, 'O', PlayerColor.RED);
        MoveRecord move3 = new MoveRecord(0, 2, 'S', PlayerColor.BLUE);
        
        assertNotEquals("Move records should be different objects", move1, move2);
        assertEquals("Move1 row should be 0", 0, move1.getRow());
        assertEquals("Move2 row should be 0", 0, move2.getRow());
        assertEquals("Move3 row should be 0", 0, move3.getRow());
        assertEquals("Move1 col should be 0", 0, move1.getCol());
        assertEquals("Move2 col should be 1", 1, move2.getCol());
        assertEquals("Move3 col should be 2", 2, move3.getCol());
    }
}

