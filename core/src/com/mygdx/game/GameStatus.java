package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;


public class GameStatus {
    private RisikoWorld world;
    private boolean turn;
    private String phase="rein";       //"rei", "att", "mov" only usefull if turn==true
    private int troopsleft=10;


    public GameStatus(TiledMap tiledMap) {
        world=new RisikoWorld(tiledMap);
        turn=true;
    }

    public void update(){
        //GET current World Object from "Server"

        // Is it your turn or not

        /* IF Server says is  your turn but only first time
        if(turn==false&& newObject.turn==true){
            troopsleft=         //number_of_countries_owned/3 but always at least 3
        }*/
    }

    public void send(){
        //Send new World Object to Server
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
