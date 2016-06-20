package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by riederch on 03.05.2016.
 * An player has an ID, Name and an Color
 */
public class Player{

    int id;
    String endpointID;
    String name;
    Color c;

/**
     * Creates an new Player with values
     * @param id
     * @param name
     * @param c
     */
    public Player(int id, String endpointID,String name) {
        this.id = id;
        this.endpointID = endpointID;
        this.name = name;

        switch (id){
            case 0: c = Color.BLUE; break;
            case 1: c = Color.ORANGE; break;
            case 2: c = Color.YELLOW; break;
            case 3: c = Color.BROWN; break;
            case 4: c = Color.GREEN; break;
            case 5: c = Color.PINK; break;
        }


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

    public String getEndpointID() {
        return endpointID;
    }

    public void setEndpointID(String endpointID) {
        this.endpointID = endpointID;
    }
}
