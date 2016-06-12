package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class GameLogic {
    private GameStatus gs;
    private Country firstcntry;
    private Country secondcntry;
    private GameScreen gamsc;
    private int time;
    private Timer timer;

    private TextureAtlas atlas = new TextureAtlas("UI/uiskin.atlas");
    private Skin skin = new Skin();
    private boolean allow = false;


    public GameLogic(GameScreen gameScreen, TiledMap tiledMap) throws IndexOutOfBoundsException {
        gs = new GameStatus(tiledMap, gameScreen.getG());
        gamsc = gameScreen;
        skin.addRegions(atlas);
        timer = new Timer();
    }

    public void reinforce(int i) {
        atlas = new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
        skin = new Skin(atlas);
        skin.load(Gdx.files.internal("UI/uiskin.json"));

        if (gs.isTurn() == true && gs.getPhase() == "rein") {
            if (firstcntry != null && secondcntry == null && firstcntry.getOwner().getId() == gamsc.getG().getP().getId()) {
                if (gs.getTroopsleft() > 0) {
                    //-------------------------------------
                    //ServerJOB
                    firstcntry.changeTroops(i);
                    //-------------------------------------
                    gs.setTroopsleft(gs.getTroopsleft() - 1);
                    gamsc.getG().getmNC().sendMessage(("4;" + firstcntry.getName()).getBytes());
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
    }

    public void attack() {
        skin.load(Gdx.files.internal("UI/uiskin.json"));
        if (gs.isTurn() == true && gs.getPhase().equals("att")) {
            if ((firstcntry != null) && (secondcntry != null) &&
                    (firstcntry.getTroops() > 1 && firstcntry != secondcntry)
                    && firstcntry.getN().get(secondcntry.getName()) != null
                    && firstcntry.getOwner().getId() == gamsc.getG().getP().getId()
                    && secondcntry.getOwner().getId() != gamsc.getG().getP().getId()) {
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

                        int pointat = 0;
                        int pointde = 0;
                        int aterg = 0;
                        int deferg = 0;

                        while (pointat < attarray.length && pointde < defarray.length) {
                            if (attarray[pointat] > defarray[pointde]) {
                                deferg = deferg - 1;
                            } else {
                                aterg = aterg - 1;
                            }
                            pointde = pointde + 1;
                            pointat = pointat + 1;
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
                                //-------------------------------------
                                //ServerJOBl
                                firstcntry.changeTroops(finalAterg);
                                secondcntry.changeTroops(finalDeferg);
                                gamsc.getG().getmNC().sendMessage(((("5;" + firstcntry.getName()) + ";" + finalAterg +
                                        ";" + secondcntry.getName() + ";" + finalDeferg).getBytes()));

                                if (secondcntry.getTroops() <= 0) {
                                    secondcntry.setOwner(firstcntry.getOwner());
                                    secondcntry.setColor(firstcntry.getOwner().getC());
                                    gamsc.getG().getmNC().sendMessage(("6;" + secondcntry.getName() + ";"
                                            + firstcntry.getOwner().getId() + ";" + firstcntry.getOwner().getName()).getBytes());

                                    if (win())
                                    {
                                        gamsc.getG().getmNC().sendMessage(("8;").getBytes());
                                        gamsc.setInputProcessorStage();
                                        final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Game Over", skin);
                                        d.scaleBy(1.2f);
                                        d.getContentTable().add("Gewinner: "+firstcntry.getOwner().getName());

                                        TextButton ok = new TextButton("OK", skin);
                                        d.getButtonTable().add(ok);

                                        ok.addListener(new InputListener() {
                                            @Override
                                            public boolean touchDown(InputEvent event, float x, float y,
                                                                     int pointer, int button) {

                                                d.hide();

                                                gamsc.getG().setScreen(new MainMenueScreen(gamsc.getG()));

                                                firstcntry = null;
                                                secondcntry = null;
                                                return true;
                                            }

                                        });
                                        d.show(gamsc.getS());
                                    }else {
                                        allow = true;
                                        move();
                                        allow = false;
                                    }
                                } else {
                                    gamsc.setInputProcessorGame();
                                    firstcntry = null;
                                    secondcntry = null;
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
                        return true;
                    }

                });
                d.show(gamsc.getS());
            }
        } else {
            throw new NullPointerException("ERROR");
        }
    }

    public boolean win() {
        boolean w = true;
        int oid = -1;
        for (ObjectMap.Entry<String, Country> country : gs.getWorld().getCountries()) {
            if (oid == -1) {
                oid = country.value.getOwner().getId();
            }

            if (oid != country.value.getOwner().getId()) {
                w = false;
                break;
            }
        }
        return w;
    }

    public void move() {
        skin.load(Gdx.files.internal("UI/uiskin.json"));
        if ((gs.isTurn() == true && gs.getPhase().equals("mov")) || allow == true) {
            Gdx.app.log("TEST", (firstcntry != null) + "&&" + (secondcntry != null) + "&&" + (firstcntry.getTroops() > 1) + "&&" + (firstcntry != secondcntry) + "||" + (allow == true) + "");

            if (((firstcntry.getTroops() > 1) && firstcntry != secondcntry
                    && firstcntry.getN().get(secondcntry.getName()) != null
                    && firstcntry.getOwner().getId()==gamsc.getG().getP().getId()
                    && secondcntry.getOwner().getId()==gamsc.getG().getP().getId())
                    || allow == true) {
                atlas = new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
                skin = new Skin(atlas);
                skin.load(Gdx.files.internal("UI/uiskin.json"));

                final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Bewegung", skin);
                d.scaleBy(1.2f);

                final Slider slide = new Slider(1, firstcntry.getTroops() - 1, 1, false, skin);

                d.getContentTable().add(firstcntry.getName() + "(" + firstcntry.getTroops() + ") --> " + secondcntry.getName() + "(" + secondcntry.getTroops() + ")");
                d.getContentTable().row();
                d.getContentTable().add(slide);
                d.getContentTable().row();
                final Label attacktroops = new Label("1", skin);
                d.getContentTable().add(attacktroops);

                TextButton btny = new TextButton("Bewegen", skin);
                d.getButtonTable().add(btny);
                if (allow != true) {
                    TextButton btnn = new TextButton("Abbrechen", skin);
                    d.getButtonTable().add(btnn);
                    btnn.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y,
                                                 int pointer, int button) {

                            Gdx.app.log("KLICK", "Nein");
                            d.hide();
                            gamsc.setInputProcessorGame();

                            firstcntry = null;
                            secondcntry = null;
                            gamsc.setInputProcessorGame();
                            return true;
                        }

                    });
                }
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
                        int movtroop = (int) slide.getValue();
                        //-------------------------------------
                        //ServerJOB
                        firstcntry.setTroops(firstcntry.getTroops() - movtroop);
                        secondcntry.setTroops(secondcntry.getTroops() + movtroop);
                        //--------------------------------------
                        gamsc.getG().getmNC().sendMessage(("7;" + firstcntry.getName() + ";"
                                + secondcntry.getName() + ";" + movtroop).getBytes());

                        d.hide();
                        firstcntry = null;
                        secondcntry = null;
                        gamsc.setInputProcessorGame();
                        return true;
                    }

                });
            } else {
                gamsc.setInputProcessorStage();
                final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Bewegung fehlgeschlagen", skin);
                d.scaleBy(1.2f);
                d.getContentTable().add("Bewegung nicht moelglich");

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
                        return true;
                    }

                });
                d.show(gamsc.getS());
            }
        } else {
            throw new NullPointerException("ERROR");
        }
    }

    public void phaseup() {
        if (getGs().isTurn()) {
            Gdx.app.log("TURN:", "______________TURN CHANGED______________");
            if (gs.getPhase().equals("rein")) {
                gs.setPhase("att");
            } else if (gs.getPhase().equals("att")) {
                gs.setPhase("mov");
            } else if (gs.getPhase().equals("mov")) {
                gs.setTurn(false);
                time = 90;
                // countdown();
                if (gamsc.getG().getmNC().ismIsHost()) {
                    gamsc.getG().getmNC().setmCurrentPlayer(0);
                    gamsc.getG().getmNC().sendMessage(gamsc.getG().getmNC().getmRemotePeerEndpoints().
                                    get(gamsc.getG().getmNC().getmCurrentPlayer()).getEndpointID(),
                            "3;".getBytes());

                } else {

                    gamsc.getG().getmNC().sendMessage("3;".getBytes());
                }
            }

        }
    }

    public void start(){
        atlas = new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
        skin = new Skin(atlas);
        skin.load(Gdx.files.internal("UI/uiskin.json"));
        gamsc.setInputProcessorStage();

        final com.badlogic.gdx.scenes.scene2d.ui.Dialog d = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Wilkommen", skin);
        d.scaleBy(1.2f);
        String s="";
        Player p=new Player(gamsc.getG().getP().getId(),null,null);
        switch (gamsc.getG().getP().getId()){
            case 0: s="BLAU"; break;
            case 1: s="ORANGE"; break;
            case 2: s="GELB"; break;
            case 3: s="BRAUM"; break;
            case 4: s="GRUEN"; break;
            case 5: s="PINK"; break;
        }

        d.getContentTable().add("Sie sind: "+ s);

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
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

    public Skin getSkin() {
        return skin;
    }
}
