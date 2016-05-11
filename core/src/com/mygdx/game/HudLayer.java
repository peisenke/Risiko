package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    /*
     *               3/4*h
     *      -------------------------------------------
     * h/6  |      |      |             |      |      |
     *      |      |      |             |      |      |
     *      -------------------------------------------
     *        w/8
     */

    private Stage s;
    private Table t;
    private  TextButton btn1;
    private  TextButton btn2;
    private  TextButton btnMid;
    private  TextButton btn3;
    private  TextButton btn4;

    public HudLayer(float w,float h){
        s=new Stage();
        t=new Table();

        btnWidth= (double)w/8;
        btnHeigth= (double)h/6;


        BitmapFont white = new BitmapFont(Gdx.files.internal("Font/white.fnt"), false);

        TextureAtlas atlas = new TextureAtlas("UI/Button.pack");
        Skin skin = new Skin(atlas);
        
        TextButton.TextButtonStyle tbs =new TextButton.TextButtonStyle();
        tbs.up=skin.getDrawable("button");
        tbs.down=skin.getDrawable("button");
        tbs.pressedOffsetX=1;
        tbs.pressedOffsetY=-1;
        tbs.font=white;
        
        btn1=new TextButton("B1",tbs);
        btn2=new TextButton("B2",tbs);
        btnMid=new TextButton("Action",tbs);
        btn3=new TextButton("B3",tbs);
        btn4=new TextButton("B4",tbs);

        t.add(btn1).size((float)btnWidth,(float)btnHeigth);
        btn1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("btn1");
            }
        });
        t.add(btn2).size((float)btnWidth,(float)btnHeigth);
        t.add(btnMid).size((float)((double)(btnWidth*2)),(float)btnHeigth);
        t.add(btn3).size((float)btnWidth,(float)btnHeigth);
        t.add(btn4).size((float)btnWidth,(float)btnHeigth);

        t.setPosition(((float)((double)w/2)),(float)((double)(btnHeigth/2)));
        s.addActor(t);
    }



    public void draw(float delta) {
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
