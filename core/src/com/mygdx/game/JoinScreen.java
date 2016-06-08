package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * Created by peise on 05.06.2016.
 */
public class JoinScreen implements Screen {
    private MyGdxGame myGame;
    private Stage s;
    private TextureAtlas atlas;
    private Skin skin;
    private Table t;
    private TextButton btncon;
    private TextButton btnback;
    private BitmapFont white;
    private BitmapFont black;
    private Label header;

    public JoinScreen(MyGdxGame g) {
        myGame = g;
    }

    @Override
    public void show() {
        s = new Stage();
        white = new BitmapFont(Gdx.files.internal("Font/white.fnt"), false);
        Gdx.input.setInputProcessor(s);
        atlas = new TextureAtlas(Gdx.files.internal("UI/uiskin.atlas"));
        skin = new Skin(atlas);
        skin.load(Gdx.files.internal("UI/uiskin.json"));

        t = new Table(skin);
        Label.LabelStyle ls = new Label.LabelStyle(white, Color.WHITE);
        header = new Label("Beitreten", ls);
        header.setFontScale(3f);

        ScrollPane.ScrollPaneStyle sps = new ScrollPane.ScrollPaneStyle(skin.getDrawable("default-rect"),
                skin.getDrawable("default-scroll"), skin.getDrawable("default-round-large"),
                skin.getDrawable("default-scroll"), skin.getDrawable("default-round-large"));

        List.ListStyle lists = new List.ListStyle(skin.getFont("default-font"), new Color(1, 1, 1, 1),
                new Color(1, 1, 1, 1), skin.getDrawable("selection"));

        lists.font.getData().setScale(3.0f);
        SelectBox.SelectBoxStyle sbs = new SelectBox.SelectBoxStyle(skin.getFont("default-font"), new Color(1, 1, 1, 1),
                skin.getDrawable("default-select"), sps, lists);


        SelectBox<java.lang.String> sb = new SelectBox<java.lang.String>(sbs);
        Array<java.lang.String> ite = new Array<java.lang.String>();
        ite.add("A");
        ite.add("A");
        ite.add("A");
        ite.add("A");
        ite.add("A");
        sb.getItems();
        sb.setItems(ite);
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = skin.getDrawable("default-round");
        tbs.down = skin.getDrawable("default-round-down");
        tbs.pressedOffsetX = 1;
        tbs.pressedOffsetY = -1;
        tbs.font = white;

        btnback = new TextButton("Zurück", tbs);
        btnback.pad(50);
        btnback.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myGame.setScreen(new MainMenueScreen(myGame));
            }
        });

        btncon = new TextButton("Verbinden", tbs);
        btncon.pad(50);
        btncon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO Warte auf Verbindung
            }
        });

        t.setFillParent(true);
        t.add(header).colspan(3);
        t.getCell(header).spaceBottom(80);
        t.row();
        t.add(sb);
        t.getCell(sb).colspan(3);
        t.getCell(sb).spaceBottom(40);
        t.row();
        t.add(btnback);
        t.getCell(btnback).fill();
        t.add();
        t.add(btncon);
        t.getCell(btncon).fill();
        t.debug();
        s.addActor(t);

        myGame.getmNC().startDiscovery();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        s.act(delta);
        s.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }


    public void addHost(Host h){
        ite.add(h.getEndpointId());
    }

    public void removeHost(String h){
        ite.removeValue(h,false);
    }
}
