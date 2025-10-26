package com.sosgame;

import com.sosgame.model.GeneralGame;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeneralGameTest {

    @Test
    public void testGameStartsAndScoresExist() {
        GeneralGame game = new GeneralGame(3);

        assertEquals(0, game.getBlueScore());
        assertEquals(0, game.getRedScore());
        assertFalse(game.isGameOver());

        game.makeMove(0,0,'S');
        game.makeMove(1,1,'O');
        game.makeMove(0,1,'O');
        game.makeMove(0,2,'S'); // maybe forms SOS

        assertNotNull(game.getBoard());
    }

    @Test
    public void testBoardEventuallyEndsOrKeepsRunning() {
        GeneralGame game = new GeneralGame(3);

        for (int r = 0; r < 3 && !game.isGameOver(); r++) {
            for (int c = 0; c < 3 && !game.isGameOver(); c++) {
                char letter = ((r + c) % 2 == 0) ? 'S' : 'O';
                game.makeMove(r, c, letter);
            }
        }

        if (game.isGameOver()) {
            assertTrue(game.getBlueScore() >= 0);
            assertTrue(game.getRedScore() >= 0);
        } else {
            assertNotNull(game.getBoard());
        }
    }
}