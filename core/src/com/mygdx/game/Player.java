package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by riederch on 03.05.2016.
 * An player has an ID, Name and an Color
 */
public class Player {

    int id;
    String name;
    Color c;

    /**
     * Creates an new Player with values
     * @param id
     * @param name
     * @param c
     */
    public Player(int id, String name, Color c) {
        this.id = id;
        this.name = name;
        this.c = c;
    }

    /**
     * Creates an default player
     */
    public Player() {
        this.id = 0;
        this.name = "Player";
        this.c = new Color(1,1,1,1f);
    }

    /**
     * Get id if the player
     * @return
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get Nameof player
     * @return playername
     */
    public String getName() {
        return name;
    }

    /**
     * set Name of Player
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Color
     * @return colorof player
     */
    public Color getC() {
        return c;
    }

    /**
     * Set new color
     * @param c
     */
    public void setC(Color c) {
        this.c = c;
    }
}
