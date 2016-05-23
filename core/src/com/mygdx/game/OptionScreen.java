package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Patrick on 08.05.2016.
 */
public class OptionScreen implements Screen {
    private MyGdxGame myGame;
    private Stage s;
    private TextureAtlas atlas;
    private Skin skin;
    private Table t;
    private TextButton btnhelp;
    private TextButton btnback;
    private BitmapFont white;
    private BitmapFont black;
    private Label header;

    public OptionScreen(MyGdxGame g){
        myGame=g;
    }
    @Override
    public void show() {
        s=new Stage();
        white=new BitmapFont(Gdx.files.internal("Font/white.fnt"), false);
        Gdx.input.setInputProcessor(s);
        atlas=new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
        skin=new Skin(atlas);
        skin.load(Gdx.files.internal("UI/uiskin.json"));

        t=new Table(skin);
        Label.LabelStyle ls=new Label.LabelStyle(white, Color.WHITE);
        header=new Label("Optionen",ls);
        header.setFontScale(3f);

        Label name=new Label("Name",skin);
        name.setFontScale(2.5f);

        TextField.TextFieldStyle tfs=new TextField.TextFieldStyle(skin.getFont("default-font"),
                new Color(1,1,1,1),skin.getDrawable("cursor"),skin.getDrawable("selection"),skin.getDrawable("textfield"));
        tfs.background.setMinHeight(name.getHeight());

        final TextField tfname=new TextField(myGame.getPref().getName()+"",tfs);

        Label music=new Label("Musik",skin);
        music.setFontScale(2.5f);
        final Label valuemusic=new Label(myGame.getPref().getMusic()+"",skin);
        valuemusic.setFontScale(2.5f);
        Label sfx=new Label("Effekte",skin);
        sfx.setFontScale(2.5f);
        final Label valuesfx=new Label(myGame.getPref().getSfx()+"", skin);
        valuesfx.setFontScale(2.5f);

        Slider.SliderStyle ss=new Slider.SliderStyle(skin.getDrawable("default-slider"),skin.getDrawable("default-slider-knob"));
        ss.background.setMinHeight(valuemusic.getHeight());
        ss.knob.setMinHeight(valuemusic.getHeight()*2f);

        final Slider slidemusic = new MySlider(0, 100, 1, false, ss);
        slidemusic.setValue(myGame.getPref().getMusic());
        slidemusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                t.getCell(valuemusic).getActor().setText(new Integer((int) slidemusic.getValue()) + "");
            }
        });

        final Slider slidesfx = new Slider(0, 100, 1, false, ss);
        slidesfx.setValue(myGame.getPref().getSfx());
        slidesfx.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                t.getCell(valuesfx).getActor().setText(new Integer((int) slidesfx.getValue()) + "");
            }
        });

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = skin.getDrawable("default-round");
        tbs.down = skin.getDrawable("default-round-down");
        tbs.pressedOffsetX = 1;
        tbs.pressedOffsetY = -1;
        tbs.font = white;

        btnback=new TextButton("Zurück",tbs);
        btnback.pad(50);
        btnback.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myGame.getPref().setName(tfname.getText());
                myGame.getP().setName(tfname.getText());
                myGame.getPref().setMusic((int) slidemusic.getValue());
                myGame.getPref().setSfx((int) slidesfx.getValue());
                myGame.setScreen(new MainMenueScreen(myGame));
            }
        });

        btnhelp=new TextButton("Hilfe", tbs);
        btnhelp.pad(50);
        btnhelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myGame.getPref().setName(tfname.getText());
                myGame.getP().setName(tfname.getText());
                myGame.getPref().setMusic((int) slidemusic.getValue());
                myGame.getPref().setSfx((int) slidesfx.getValue());
            }
        });

        t.setFillParent(true);
        t.add(header).colspan(3);
        t.getCell(header).spaceBottom(40);
        t.row();
        t.add(name);
        t.getCell(name).spaceBottom(10);
        t.add(tfname);
        t.getCell(tfname).colspan(2);
        t.getCell(tfname).spaceBottom(10);
        t.row();
        t.add(music);
        t.getCell(music).spaceBottom(10);
        t.add(slidemusic);
        t.getCell(slidemusic).spaceBottom(10);
        t.getCell(slidemusic).fill();
        t.add(valuemusic);
        t.getCell(valuemusic).spaceBottom(10);
        t.row();
        t.add(sfx);
        t.getCell(sfx).spaceBottom(30);
        t.add(slidesfx);
        t.getCell(slidesfx).spaceBottom(30);
        t.getCell(slidesfx).fill();
        t.add(valuesfx);
        t.getCell(valuesfx).spaceBottom(30);
        t.row();
        t.add(btnback);
        t.getCell(btnback).fill();
        t.add();
        t.add(btnhelp);
        t.getCell(btnhelp).fill();
        s.addActor(t);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        s.act(delta);
        s.draw();
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
