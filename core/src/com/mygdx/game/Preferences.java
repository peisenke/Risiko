package com.mygdx.game;

/**
 * Created by Patrick on 08.05.2016.
 * Saves the settings of the game including name, volume of music and sound
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

    /**
     * Get Playername
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set Playername
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get volume of Music
     * @return music
     */
    public int getMusic() {
        return music;
    }

    /**
     * set volume of music
     * @param music
     */
    public void setMusic(int music) {
        this.music = music;
    }

    /**
     * Get volume of sounds
     * @return sfx
     */
    public int getSfx() {
        return sfx;
    }

    /**
     * Set volume of Sounds
     * @param sfx
     */
    public void setSfx(int sfx) {
        this.sfx = sfx;
    }
}
