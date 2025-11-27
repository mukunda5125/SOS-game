package com.sosgame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sosgame.model.GameMode;
import com.sosgame.model.GameRecord;
import com.sosgame.model.MoveRecord;
import com.sosgame.model.PlayerColor;

import java.util.List;

/**
 * JUnit tests for GameRecord class.
 * Tests creating empty and non-empty record lists, and addMove() functionality.
 */
public class GameRecordTest {

    private GameRecord record;

    @Before
    public void setUp() {
        record = new GameRecord();
    }

    @Test
    public void testCreateEmptyRecord() {
        assertNotNull("GameRecord should not be null", record);
        assertNotNull("Moves list should be initialized", record.getMoves());
        assertEquals("Moves list should be empty", 0, record.getMoves().size());
    }

    @Test
    public void testSettersAndGetters() {
        record.setMode(GameMode.SIMPLE);
        record.setBoardSize(5);
        record.setBlueIsComputer(true);
        record.setRedIsComputer(false);
        
        assertEquals("Mode should be SIMPLE", GameMode.SIMPLE, record.getMode());
        assertEquals("Board size should be 5", 5, record.getBoardSize());
        assertTrue("Blue should be computer", record.isBlueIsComputer());
        assertFalse("Red should not be computer", record.isRedIsComputer());
    }

    @Test
    public void testAddMoveToEmptyRecord() {
        MoveRecord move = new MoveRecord(0, 0, 'S', PlayerColor.BLUE);
        record.addMove(move);
        
        List<MoveRecord> moves = record.getMoves();
        assertEquals("Moves list should have 1 move", 1, moves.size());
        assertEquals("First move should match", move, moves.get(0));
    }

    @Test
    public void testAddMultipleMoves() {
        MoveRecord move1 = new MoveRecord(0, 0, 'S', PlayerColor.BLUE);
        MoveRecord move2 = new MoveRecord(0, 1, 'O', PlayerColor.RED);
        MoveRecord move3 = new MoveRecord(0, 2, 'S', PlayerColor.BLUE);
        
        record.addMove(move1);
        record.addMove(move2);
        record.addMove(move3);
        
        List<MoveRecord> moves = record.getMoves();
        assertEquals("Moves list should have 3 moves", 3, moves.size());
        assertEquals("First move should match", move1, moves.get(0));
        assertEquals("Second move should match", move2, moves.get(1));
        assertEquals("Third move should match", move3, moves.get(2));
    }

    @Test
    public void testAddMovePreservesOrder() {
        // Add moves in sequence
        for (int i = 0; i < 5; i++) {
            PlayerColor player = (i % 2 == 0) ? PlayerColor.BLUE : PlayerColor.RED;
            char letter = (i % 2 == 0) ? 'S' : 'O';
            MoveRecord move = new MoveRecord(0, i, letter, player);
            record.addMove(move);
        }
        
        List<MoveRecord> moves = record.getMoves();
        assertEquals("Should have 5 moves", 5, moves.size());
        
        // Verify order
        for (int i = 0; i < 5; i++) {
            MoveRecord move = moves.get(i);
            assertEquals("Move " + i + " should be at column " + i, i, move.getCol());
        }
    }

    @Test
    public void testSetMoves() {
        List<MoveRecord> moves = new java.util.ArrayList<>();
        moves.add(new MoveRecord(0, 0, 'S', PlayerColor.BLUE));
        moves.add(new MoveRecord(0, 1, 'O', PlayerColor.RED));
        
        record.setMoves(moves);
        
        List<MoveRecord> retrievedMoves = record.getMoves();
        assertEquals("Should have 2 moves", 2, retrievedMoves.size());
        assertEquals("First move should match", moves.get(0), retrievedMoves.get(0));
        assertEquals("Second move should match", moves.get(1), retrievedMoves.get(1));
    }

    @Test
    public void testGeneralModeRecord() {
        record.setMode(GameMode.GENERAL);
        record.setBoardSize(4);
        record.setBlueIsComputer(false);
        record.setRedIsComputer(true);
        
        assertEquals("Mode should be GENERAL", GameMode.GENERAL, record.getMode());
        assertEquals("Board size should be 4", 4, record.getBoardSize());
        assertFalse("Blue should not be computer", record.isBlueIsComputer());
        assertTrue("Red should be computer", record.isRedIsComputer());
    }
}

