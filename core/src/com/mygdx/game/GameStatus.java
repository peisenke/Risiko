package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;


public class GameStatus {
    private RisikoWorld world;
    private boolean turn;
    private String phase="rein";       //"rein", "att", "mov" only usefull if turn==true
    private int troopsleft=10;


    public GameStatus(TiledMap tiledMap, MyGdxGame g) throws IndexOutOfBoundsException{
        world=new RisikoWorld(tiledMap,g);
        turn=true;
    }

    public GameStatus() {
        turn=true;
    }

    public int getTroopsleft() {
        return troopsleft;
    }

    public void setTroopsleft(int troopsleft) {
        this.troopsleft = troopsleft;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public RisikoWorld getWorld() {
        return world;
    }

    public void setWorld(RisikoWorld world) {
        this.world = world;
    }
}
