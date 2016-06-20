package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;


public class GameStatus {
    private RisikoWorld world;
    private boolean turn;
    private String phase="rein";       //"rein", "att", "mov" only usefull if turn==true
    private int troopsleft=0;
    private MyGdxGame ga;


    public GameStatus(TiledMap tiledMap, MyGdxGame g) throws IndexOutOfBoundsException{
        world=new RisikoWorld(tiledMap,g);
        ga=g;
        turn=false;
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
        if (turn){
            int cnt=0;
            ArrayMap<String, Country> x = world.getCountries();
            for (ObjectMap.Entry<String, Country> c : x) {
                if (c.value.getOwner().getId()==ga.getP().getId()){
                    cnt++;
                }
            }
            int troops=cnt/3;

            if(troops<3){
                troops=3;
            }
            troopsleft= troops;     //number_of_countries_owned/3 but always at least 3
        }
        setPhase("rein");
        this.turn = turn;
    }

    public RisikoWorld getWorld() {
        return world;
    }

    public void setWorld(RisikoWorld world) {
        this.world = world;
    }
}