package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by riederch on 10.05.2016.
 */
public class HudLayer {
    private double btnHeigth;
    private double btnWidth;
    private TextureAtlas atlas;
    private Skin skin;
    /*
     *               3/4*h
     *      -------------------------------------------
     * h/6  |      |      |             |      |      |
     *      |      |      |             |      |      |
     *      -------------------------------------------
     *        w/8
     */

    private Stage s;
    private Table ttop;
    private Table tbot;
    private  TextButton btnweiter;
    private Label lbtroops;
    private Label lbtime;
    private Label lbphase;
    private GameLogic glo;
private Label troops;

    public HudLayer(float w, float h, GameLogic gl){
        s=new Stage();
        glo=gl;
        ttop=new Table();
        tbot=new Table();

        btnWidth= (double)w/8;
        btnHeigth= (double)h/8;


        atlas = new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
        skin = new Skin(atlas);
        skin.load(Gdx.files.internal("UI/uiskin.json"));
        
        TextButton.TextButtonStyle tbs =new TextButton.TextButtonStyle();
        tbs.up = skin.getDrawable("default-round");
        tbs.down = skin.getDrawable("default-round-down");
        tbs.pressedOffsetX = 1;
        tbs.pressedOffsetY = -1;
        tbs.font = skin.getFont("default-font");

        btnweiter=new TextButton("Weiter",tbs);
        btnweiter.getLabel().setFontScale(2f);

        tbot.add(btnweiter).size((float)btnWidth,(float)btnHeigth);
        btnweiter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               glo.phaseup();
            }
        });
        lbtroops=new Label("Truppen 2",skin);
        lbtroops.setFontScale(2.5f);
        tbot.add(lbtroops);
        tbot.getCell(btnweiter).padRight(40);

        lbphase=new Label("Truppen 2",skin);
        lbphase.setFontScale(2.5f);
        ttop.add(lbphase);
        lbtime=new Label("Truppen 2",skin);
        lbtime.setFontScale(2.5f);
        ttop.add(lbtime);
        ttop.getCell(lbphase).padRight(40);

        tbot.setPosition(((float)((double)w/2)),(float)((double)(btnHeigth/2)));
        ttop.setPosition(((float)((double)w/2)),(float)((double)(h-(btnHeigth/2))));
        s.addActor(ttop);
        s.addActor(tbot);
    }



    public void draw(float delta) {
        lbtroops.setText("Truppen: "+ glo.getGs().getTroopsleft());
        if (glo.getGs().getPhase().equals("rein")) {
            lbphase.setText("Verstaerkung");
        } else if (glo.getGs().getPhase().equals("att")) {
            lbphase.setText("Angriff");
        } else if (glo.getGs().getPhase().equals("mov")) {
            lbphase.setText("Bewegung");
        }
        if(glo.getTime()>=600){
            if(glo.getTime()%60<10){
                lbtime.setText(glo.getTime()/60 +":0" + glo.getTime()%60);
            }else {
                lbtime.setText(glo.getTime()/60 +":" + glo.getTime()%60);
            }
        }else {
            if(glo.getTime()%60<10){
                lbtime.setText("0" + glo.getTime()/60 +":0" + glo.getTime()%60);
            }else {
                lbtime.setText("0" + glo.getTime()/60 +":" + glo.getTime()%60);
            }
        }

        s.act(delta);
        s.draw();
    }

    public Stage getStage() {
        return s;
    }

    public float getHeigth() {
        return (float)this.btnHeigth;
    }
}