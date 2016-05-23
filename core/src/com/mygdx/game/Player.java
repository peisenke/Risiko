package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by riederch on 03.05.2016.
 */
public class Player {

    int id;
    String name;
    Color c;

    public Player(int id, String name, Color c) {
        this.id = id;
        this.name = name;
        this.c = c;
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
}
