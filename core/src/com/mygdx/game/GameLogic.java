package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Arrays;

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
    private boolean allow=false;


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

                    int atttroops = (int) d.getContentTable().getCell(slide).getActor().getValue();
                    int deftroops = secondcntry.getTroops();
                    if (deftroops > 2) {
                        deftroops = 2;
                    }

                    int[] attarray = new int[atttroops];
                    int[] defarray = new int[deftroops];

                    for (int i = 0; i < attarray.length; i++) {
                        attarray[i] = MathUtils.random(1, 6);
                    }

                    for (int j = 0; j < defarray.length; j++) {
                        defarray[j] = MathUtils.random(1, 6);
                    }
                    Arrays.sort(attarray);
                    Arrays.sort(defarray);

                    int[] tmp = new int[attarray.length];
                    int cnt = 0;
                    for (int i = attarray.length - 1; i >= 0; i--) {
                        tmp[cnt] = attarray[i];
                        cnt++;
                    }
                    attarray = tmp.clone();

                    int[] tmp2 = new int[defarray.length];
                    cnt = 0;
                    for (int i = defarray.length - 1; i >= 0; i--) {
                        tmp2[cnt] = defarray[i];
                        cnt++;
                    }
                    defarray = tmp2.clone();


                    for (int i = 0; i < attarray.length; i++) {
                        Gdx.app.log("ATTACKARRAY", attarray[i] + "");
                    }

                    for (int i = 0; i < defarray.length; i++) {
                        Gdx.app.log("DEFARRAY", defarray[i] + "");
                    }


                    gamsc.setInputProcessorStage();
                    final com.badlogic.gdx.scenes.scene2d.ui.Dialog d2 = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Angriffsberechnung", skin);
                    d2.scaleBy(1.2f);

                    String at = "";
                    String de = "";


                    for (int i = 0; i < attarray.length; i++) {
                        at = at + "[" + attarray[i] + "]" + "  ";
                    }

                    for (int i = 0; i < defarray.length; i++) {
                        de = de + "[" + defarray[i] + "]" + "  ";
                    }

                    d2.getContentTable().add("Angreifer: " + at);
                    d2.getContentTable().row();
                    d2.getContentTable().add("Verteidiger: " + de);

                    int pointat=0;
                    int pointde=0;
                    int aterg=0;
                    int deferg=0;

                    while(pointat<attarray.length && pointde<defarray.length)
                    {
                        if(attarray[pointat]>defarray[pointde]){
                            deferg=deferg-1;
                        }else{
                            aterg=aterg-1;
                        }
                        pointde=pointde+1;
                        pointat=pointat+1;
                    }

                    d2.getContentTable().row();
                    d.getContentTable().add("   ");
                    d2.getContentTable().row();
                    d2.getContentTable().add("Ergebnis Angreifer: " + aterg);
                    d2.getContentTable().row();
                    d2.getContentTable().add("Ergebnis Verteidiger: " + deferg);


                    TextButton ok = new TextButton("OK", skin);
                    d2.getButtonTable().add(ok);

                    final int finalAterg = aterg;
                    final int finalDeferg = deferg;
                    ok.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y,
                                                 int pointer, int button) {

                            d2.hide();
                            firstcntry.changeTroops(finalAterg);
                            secondcntry.changeTroops(finalDeferg);
                            if(secondcntry.getTroops()<=0){
                                //TODO Change Owner
                                allow=true;
                                move();
                                allow=false;
                            }else {
                                gamsc.setInputProcessorGame();
                                firstcntry = null;
                                secondcntry = null;
                                gs.update();
                            }
                            return true;
                        }
                    });
                    d2.show(gamsc.getS());
                    d.hide();
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

                    firstcntry = null;
                    secondcntry = null;
                    gs.update();
                    return true;
                }

            });
        } else {
            gamsc.setInputProcessorStage();
            final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Angriff fehlgeschlagen", skin);
            d.scaleBy(1.2f);
            d.getContentTable().add("Angriff nicht moelglich");

            TextButton ok = new TextButton("OK", skin);
            d.getButtonTable().add(ok);

            ok.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y,
                                         int pointer, int button) {

                    d.hide();
                    gamsc.setInputProcessorGame();

                    firstcntry = null;
                    secondcntry = null;
                    gs.update();
                    return true;
                }

            });
            d.show(gamsc.getS());
        }
    }

    public void move() {
        if ((firstcntry != null && secondcntry == null && firstcntry.getTroops() < 1/* && firstcntry.getOwner()==me && secondcntry.getOwner()=me*/)|| allow==true) {
            //Open Dialog with slider from 1 to firstcntry.troops-1
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
