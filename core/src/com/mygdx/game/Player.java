package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by riederch on 03.05.2016.
 */
public class Player{

    int id;
    String endpointID;
    String name;
    Color c;

    public Player(int id, String endpointID,String name) {
        this.id = id;
        this.endpointID = endpointID;
        this.name = name;

        switch (id){
            case 1: c = Color.BLUE; break;
            case 2: c = Color.RED; break;
            case 3: c = Color.YELLOW; break;
            case 4: c = Color.BROWN; break;
            case 5: c = Color.GREEN; break;
            case 6: c = Color.PINK; break;
        }


    }

    public Player() {
        this.id = 0;
        this.name = "Player";
        this.c = new Color(1,1,1,1f);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getC() {
        return c;
    }

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
