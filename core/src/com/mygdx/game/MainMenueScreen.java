package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Patrick on 14.04.2016.
 */
public class MainMenueScreen implements Screen {
    private Game myGame;
    private Stage s;
    private TextureAtlas atlas;
    private Skin skin;
    private Table t;
    private TextButton btnhostgame;
    private TextButton btnjoingame;
    private TextButton btnoptions;
    private BitmapFont white;
    private BitmapFont black;
    private Label header;

    public MainMenueScreen(Game g){
        myGame=g;
    }
    @Override
    public void show() {
        s=new Stage();
        white=new BitmapFont(Gdx.files.internal("Font/white.fnt"), false);
        black=new BitmapFont(Gdx.files.internal("Font/black.fnt"), false);

        Gdx.input.setInputProcessor(s);
        atlas=new TextureAtlas("UI/Button.pack");
        skin=new Skin(atlas);

        t=new Table(skin);
        //t.setBounds(Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/5,Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/5);
        TextButton.TextButtonStyle tbs =new TextButton.TextButtonStyle();
        tbs.up=skin.getDrawable("button");
        tbs.down=skin.getDrawable("button");
        tbs.pressedOffsetX=1;
        tbs.pressedOffsetY=-1;
        tbs.font=white;

        Label.LabelStyle ls=new Label.LabelStyle(white, Color.WHITE);

        header=new Label("Risiko",ls);
        header.setFontScale(2.5f);

        btnhostgame=new TextButton("Spiel erstellen",tbs);
        btnhostgame.pad(50);
        btnhostgame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myGame.setScreen(new GameScreen(myGame));
            }
        });


        btnjoingame=new TextButton("Spiel beitreten", tbs);
        btnjoingame.pad(50);

        btnoptions=new TextButton("Optionen", tbs);
        btnoptions.pad(50);

        t.setFillParent(true);
        t.add(header).colspan(3);
        t.getCell(header).spaceBottom(40);
        t.row();
        t.add(btnhostgame);
        t.getCell(btnhostgame).spaceBottom(10);
        t.row();
        t.add(btnjoingame);
        t.getCell(btnjoingame).spaceBottom(10);
        t.row();
        t.add(btnoptions);
        t.getCell(btnoptions).spaceBottom(10);

    //    t.debug();
        s.addActor(t);



        }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        s.act(delta);
        s.draw();
/*
        if(Gdx.input.justTouched())
            myGame.setScreen(new GameScreen(myGame));
   */
    }

    @Override
    public void resize(int width, int height) {
        s.act();
        s.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlas.dispose();
        s.dispose();
        skin.dispose();
        white.dispose();
        black.dispose();

    }
}
