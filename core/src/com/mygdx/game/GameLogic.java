package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Patrick on 29.04.2016.
 */
public class GameLogic {
    private GameStatus gs;
    private Country firstcntry;
    private Country secondcntry;
    private GameScreen gamsc;

    TextureAtlas atlas = new TextureAtlas("UI/uiskin.atlas");
    Skin skin = new Skin();


    public GameLogic(GameScreen gameScreen, TiledMap tiledMap) {
        gs = new GameStatus(tiledMap);
        gamsc = gameScreen;
        skin.addRegions(atlas);
    }

    public void reinforce(int i) {
        atlas = new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
        skin = new Skin(atlas);
        skin.load(Gdx.files.internal("UI/uiskin.json"));

        if (gs.isTurn() == true && gs.getPhase() == "rein") {
            if (firstcntry != null && secondcntry == null/* TODO: && firstcntry.getOwner()==me*/) {
                if (gs.getTroopsleft() > 0) {
                    firstcntry.changeTroops(i);
                    gs.setTroopsleft(gs.getTroopsleft() - 1);
                } else {


                    gamsc.setInputProcessorStage();
                    final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Keine Truppen", skin);
                    d.scaleBy(1.2f);
                    d.getContentTable().add("Sie haben keine Truppen mehr");

                    TextButton ok = new TextButton("OK", skin);
                    d.getButtonTable().add(ok);

                    ok.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y,
                                                 int pointer, int button) {

                            d.hide();
                            gamsc.setInputProcessorGame();
                            return true;
                        }

                    });
                    d.show(gamsc.getS());
                }
            } else {
                //TODO: Test this else
                gamsc.setInputProcessorStage();
                final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Nicht ihr Land", skin);
                d.scaleBy(1.2f);
                d.getContentTable().add("Sie besitzen dieses Land nicht");

                TextButton ok = new TextButton("OK", skin);
                d.getButtonTable().add(ok);

                ok.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y,
                                             int pointer, int button) {

                        d.hide();
                        gamsc.setInputProcessorGame();
                        return true;
                    }

                });
                d.show(gamsc.getS());
            }
        } else {
            throw new NullPointerException("ERROR");
        }

        firstcntry = null;
        secondcntry = null;
        gs.update();
    }

    public void attack() {
        if ((firstcntry != null) && (secondcntry != null) && (firstcntry.getTroops() > 1)/* TODO && firstcntry.getOwner()==me && secondcntry.getOwner()!=me*/) {
            atlas = new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
            skin = new Skin(atlas);
            skin.load(Gdx.files.internal("UI/uiskin.json"));

            final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Angriff", skin);
            d.scaleBy(1.2f);

            int max = firstcntry.getTroops() - 1;

            if (max > 3) {
                max = 3;
            }
            final Slider slide = new Slider(1, max, 1, false, skin);

            d.getContentTable().add(firstcntry.getName() + "(" + firstcntry.getTroops() + ") --> " + secondcntry.getName() + "(" + secondcntry.getTroops() + ")");
            d.getContentTable().row();
            d.getContentTable().add(slide);
            d.getContentTable().row();
            final Label attacktroops = new Label("1", skin);
            d.getContentTable().add(attacktroops);

            TextButton btny = new TextButton("Angriff", skin);
            TextButton btnn = new TextButton("Abbrechen", skin);
            d.getButtonTable().add(btny);
            d.getButtonTable().add(btnn);
            d.show(gamsc.getS());

            slide.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    d.getContentTable().getCell(attacktroops).getActor().setText(new Integer((int) slide.getValue()) + "");
                }
            });

            btny.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y,
                                         int pointer, int button) {

                    Gdx.app.log("KLICK", "Ja");

                    int atttroops = (int) d.getContentTable().getCell(slide).getActor().getValue();

                    Gdx.app.log("Test: ", "" + secondcntry);
                    int erg = atttroops - secondcntry.getTroops();
                    Gdx.app.log("ERG: ", "" + erg);

                    //TODO Kampfberechnung
         /*   if(erg<0){
                firstcntry.changeTroops((-(firstcntry.getTroops()))+1);
                secondcntry.changeTroops((-(secondcntry.getTroops()))+(-(erg)));
            }else{
                secondcntry.changeTroops((-(firstcntry.getTroops()))+erg);
                firstcntry.changeTroops((-(firstcntry.getTroops()))+1);
            }*/
                    d.hide();
                    gamsc.setInputProcessorGame();

                    firstcntry = null;
                    secondcntry = null;
                    gs.update();


                    return true;
                }

            });

            btnn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y,
                                         int pointer, int button) {

                    Gdx.app.log("KLICK", "Nein");
                    d.hide();
                    gamsc.setInputProcessorGame();
                    return true;
                }

            });
        } else {
            //ERROR
        }
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
