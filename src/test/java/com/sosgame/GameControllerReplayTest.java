package com.sosgame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sosgame.controller.GameController;
import com.sosgame.model.ComputerPlayer;
import com.sosgame.model.GameMode;
import com.sosgame.model.GameRecord;
import com.sosgame.model.HumanPlayer;
import com.sosgame.model.Player;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * JUnit tests for GameController replay functionality.
 * Tests isReplayMode(), startReplay(), and replay completion.
 */
public class GameControllerReplayTest {

    private GameController controller;
    private File tempFile;

    @Before
    public void setUp() throws IOException {
        controller = new GameController();
        tempFile = File.createTempFile("sos_test_replay", ".txt");
        tempFile.deleteOnExit();
    }

    @Test
    public void testIsReplayModeReturnsFalseInitially() {
        assertFalse("Replay mode should be false initially", controller.isReplayMode());
    }

    @Test
    public void testIsReplayModeReturnsTrueDuringReplay() throws IOException, InterruptedException {
        // Create a recording
        createTestRecording();
        
        // Load recording
        controller.loadRecordingFromFile(tempFile);
        
        // Set up game
        setupGameFromRecording();
        
        // Start replay
        CountDownLatch latch = new CountDownLatch(1);
        controller.startReplay(() -> latch.countDown());
        
        // Wait a bit for replay to start
        Thread.sleep(100);
        
        // Check replay mode
        assertTrue("Replay mode should be true during replay", controller.isReplayMode());
        
        // Wait for replay to finish
        latch.await(10, TimeUnit.SECONDS);
        
        // Replay mode should be false after completion
        assertFalse("Replay mode should be false after completion", controller.isReplayMode());
    }

    @Test
    public void testStartReplayReplaysMovesInCorrectOrder() throws IOException, InterruptedException {
        // Create a recording with known moves
        controller.startRecording(GameMode.SIMPLE, 3, false, false);
        Player bluePlayer = new HumanPlayer("Blue", controller);
        Player redPlayer = new HumanPlayer("Red", controller);
        controller.setPlayers(bluePlayer, redPlayer);
        controller.startNewGame(3, GameMode.SIMPLE);
        
        // Make specific moves in order
        controller.tryMove(0, 0, 'S'); // Blue at (0,0)
        controller.tryMove(0, 1, 'O'); // Red at (0,1)
        controller.tryMove(0, 2, 'S'); // Blue at (0,2)
        
        // Save recording
        controller.saveRecordingToFile(tempFile);
        
        // Load and replay
        controller.loadRecordingFromFile(tempFile);
        setupGameFromRecording();
        
        // Clear the board for replay
        controller.startNewGame(3, GameMode.SIMPLE);
        
        CountDownLatch latch = new CountDownLatch(1);
        controller.startReplay(() -> {
            latch.countDown();
        });
        
        // Wait for replay to complete
        boolean completed = latch.await(10, TimeUnit.SECONDS);
        assertTrue("Replay should complete within timeout", completed);
        
        // Verify moves were applied
        char[][] board = controller.getBoardAsCharMatrix();
        assertEquals("First move should be S at (0,0)", 'S', board[0][0]);
        assertEquals("Second move should be O at (0,1)", 'O', board[0][1]);
        assertEquals("Third move should be S at (0,2)", 'S', board[0][2]);
    }

    @Test
    public void testReplayEndsWhenAllMovesAreConsumed() throws IOException, InterruptedException {
        // Create a recording with 3 moves
        controller.startRecording(GameMode.SIMPLE, 3, false, false);
        Player bluePlayer = new HumanPlayer("Blue", controller);
        Player redPlayer = new HumanPlayer("Red", controller);
        controller.setPlayers(bluePlayer, redPlayer);
        controller.startNewGame(3, GameMode.SIMPLE);
        
        // Make exactly 3 moves
        controller.tryMove(0, 0, 'S');
        controller.tryMove(0, 1, 'O');
        controller.tryMove(0, 2, 'S');
        
        // Save recording
        controller.saveRecordingToFile(tempFile);
        
        // Load and replay
        controller.loadRecordingFromFile(tempFile);
        GameRecord record = controller.getReplayRecord();
        int expectedMoves = record.getMoves().size();
        
        setupGameFromRecording();
        controller.startNewGame(3, GameMode.SIMPLE);
        
        // Use latch to detect when replay finishes
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] replayFinished = {false};
        
        controller.startReplay(() -> {
            replayFinished[0] = true;
            latch.countDown();
        });
        
        // Wait for replay to complete (should take about expectedMoves seconds)
        boolean completed = latch.await(expectedMoves * 2 + 2, TimeUnit.SECONDS);
        
        assertTrue("Replay should complete", completed);
        assertTrue("Replay finished callback should be called", replayFinished[0]);
        assertFalse("Replay mode should be false after completion", controller.isReplayMode());
    }

    @Test
    public void testReplayCallbackIsCalled() throws IOException, InterruptedException {
        // Create a simple recording
        createTestRecording();
        
        // Load and setup
        controller.loadRecordingFromFile(tempFile);
        setupGameFromRecording();
        controller.startNewGame(3, GameMode.SIMPLE);
        
        // Track if callback is called
        final boolean[] callbackCalled = {false};
        CountDownLatch latch = new CountDownLatch(1);
        
        controller.startReplay(() -> {
            callbackCalled[0] = true;
            latch.countDown();
        });
        
        // Wait for completion
        boolean completed = latch.await(10, TimeUnit.SECONDS);
        
        assertTrue("Replay should complete", completed);
        assertTrue("Callback should be called", callbackCalled[0]);
    }

    @Test
    public void testReplayWithGeneralMode() throws IOException, InterruptedException {
        // Create recording in General mode
        controller.startRecording(GameMode.GENERAL, 3, false, false);
        Player bluePlayer = new HumanPlayer("Blue", controller);
        Player redPlayer = new HumanPlayer("Red", controller);
        controller.setPlayers(bluePlayer, redPlayer);
        controller.startNewGame(3, GameMode.GENERAL);
        
        // Make moves
        controller.tryMove(0, 0, 'S');
        controller.tryMove(0, 1, 'O');
        
        // Save
        controller.saveRecordingToFile(tempFile);
        
        // Load and replay
        controller.loadRecordingFromFile(tempFile);
        setupGameFromRecording();
        controller.startNewGame(3, GameMode.GENERAL);
        
        CountDownLatch latch = new CountDownLatch(1);
        controller.startReplay(() -> latch.countDown());
        
        boolean completed = latch.await(10, TimeUnit.SECONDS);
        assertTrue("Replay should complete", completed);
        assertFalse("Replay mode should be false after completion", controller.isReplayMode());
    }

    // Helper method to create a test recording
    private void createTestRecording() throws IOException {
        controller.startRecording(GameMode.SIMPLE, 3, false, false);
        Player bluePlayer = new HumanPlayer("Blue", controller);
        Player redPlayer = new HumanPlayer("Red", controller);
        controller.setPlayers(bluePlayer, redPlayer);
        controller.startNewGame(3, GameMode.SIMPLE);
        
        // Make a few moves
        controller.tryMove(0, 0, 'S');
        controller.tryMove(0, 1, 'O');
        
        controller.saveRecordingToFile(tempFile);
    }

    // Helper method to set up game from loaded recording
    private void setupGameFromRecording() {
        GameRecord record = controller.getReplayRecord();
        if (record == null) return;
        
        Player bluePlayer = record.isBlueIsComputer()
            ? new ComputerPlayer("Blue", controller)
            : new HumanPlayer("Blue", controller);
        Player redPlayer = record.isRedIsComputer()
            ? new ComputerPlayer("Red", controller)
            : new HumanPlayer("Red", controller);
        controller.setPlayers(bluePlayer, redPlayer);
    }
}

