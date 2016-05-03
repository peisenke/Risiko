package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by Patrick on 29.04.2016.
 */
public class GameLogic {
    private GameStatus gs;
    private Country firstcntry;
    private Country secondcntry;
    private GameScreen gamsc;

    public GameLogic(GameScreen gameScreen, TiledMap tiledMap) {
        gs = new GameStatus(tiledMap);
        gamsc = gameScreen;
    }

    public void reinforce(int i) {
        if (gs.isTurn() == true && gs.getPhase() == "rein") {
            if (firstcntry != null && secondcntry == null/* && firstcntry.getOwner()==me*/) {
                firstcntry.changeTroops(i);
            } else {
                //ERROR
            }
        } else {
            //ERROR
        }

        firstcntry = null;
        secondcntry = null;
        gs.update();
    }

    public void attack() {

        Gdx.app.log("ERG: ", ""+firstcntry.getTroops());
        if ((firstcntry != null) && (secondcntry != null) && (firstcntry.getTroops()> 1)/* && firstcntry.getOwner()==me && secondcntry.getOwner()!=me*/) {
            //Open Dialog with checkbox 1, 2 or 3
            int erg=firstcntry.getTroops()-1 - secondcntry.getTroops();
            Gdx.app.log("ERG: ", ""+erg);
         /*   if(erg<0){
                firstcntry.changeTroops((-(firstcntry.getTroops()))+1);
                secondcntry.changeTroops((-(secondcntry.getTroops()))+(-(erg)));
            }else{
                secondcntry.changeTroops((-(firstcntry.getTroops()))+erg);
                firstcntry.changeTroops((-(firstcntry.getTroops()))+1);
            }*/
        } else {
            //ERROR
        }
        firstcntry = null;
        secondcntry = null;
        gs.update();
    }

    public void move() {
        if (firstcntry != null && secondcntry == null && firstcntry.getTroops() < 1/* && firstcntry.getOwner()==me && secondcntry.getOwner()=me*/) {
            //Open Dialog with slider from 1 to seconcntry.troops-1
        } else {
            //ERROR
        }
        firstcntry = null;
        secondcntry = null;
        gs.update();
    }

    public GameStatus getGs() {
        return gs;
    }

    public void setGs(GameStatus gs) {
        this.gs = gs;
    }

    public Country getFirstcntry() {
        return firstcntry;
    }

    public void setFirstcntry(Country firstcntry) {
        this.firstcntry = firstcntry;
    }

    public Country getSecondcntry() {
        return secondcntry;
    }

    public void setSecondcntry(Country secondcntry) {
        this.secondcntry = secondcntry;
    }

    public GameScreen getGamsc() {
        return gamsc;
    }

    public void setGamsc(GameScreen gams) {
        this.gamsc = gams;
    }
}
