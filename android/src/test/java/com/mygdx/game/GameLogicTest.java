package com.mygdx.game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogicTest {

    private GameLogic gameLogic;

    @Before
    public void setUp() throws Exception {
        // TODO: GameLogic(GameScreen gameScreen, TiledMap tiledMap)
        gameLogic = new GameLogic(null, null);
    }

    @Test
    public void testReinforce() throws Exception {
        gameLogic.getGs().setTroopsleft(10);
        gameLogic.reinforce(1);
        //expected: 9, 10 troops - 1 troop after reinforce
        assertEquals(9, gameLogic.getGs().getTroopsleft(), 0);
    }

    @Test
    public void testAttack() throws Exception {

    }

    @Test
    public void testMove() throws Exception {

    }

    @Test
    public void testPhaseup() throws Exception {

    }

    @Test
    public void testCountdown() throws Exception {

    }

    @Test
    public void testGetTime() throws Exception {

    }

    @Test
    public void testSetTime() throws Exception {

    }

    @Test
    public void testGetGs() throws Exception {

    }

    @Test
    public void testSetGs() throws Exception {

    }

    @Test
    public void testGetFirstcntry() throws Exception {

    }

    @Test
    public void testSetFirstcntry() throws Exception {

    }

    @Test
    public void testGetSecondcntry() throws Exception {

    }

    @Test
    public void testSetSecondcntry() throws Exception {

    }

    @Test
    public void testGetGamsc() throws Exception {

    }

    @Test
    public void testSetGamsc() throws Exception {

    }
}