package com.mygdx.game;

/**
 * Created by Patrick on 08.05.2016.
 */
public class Preferences {
    private String name;
    private int music;
    private int sfx;

    public Preferences(){
        name="Player";
        music=100;
        sfx=100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public int getSfx() {
        return sfx;
    }

    public void setSfx(int sfx) {
        this.sfx = sfx;
    }
}
