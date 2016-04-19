package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import sun.rmi.runtime.Log;

/**
 * Created by Patrick on 14.04.2016.
 */
public class GameScreen implements Screen,InputProcessor {
    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    private Vector2 lastTouch=new Vector2();
    private ShapeRenderer sr;
    float w = 0;
    float h = 0;
    Game g;
    int mapwidth=0;
    int mapheight=0;


    public GameScreen(Game g){
        this.g=g;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        tiledMap = new TmxMapLoader().load("Map/Risiko-map.tmx");
        mapwidth=tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);
        mapheight=tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor((InputProcessor) this);
        sr=new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        w=width;
        h=height;
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


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 newTouch = new Vector2(screenX, screenY);
        Vector2 delta = newTouch.cpy().sub(lastTouch);
        float xmin=w/2;
        float xmax=mapwidth-(w/2);
        float ymin=h/2;
        float ymax=mapheight-(h/2);

        /*
        Vector3 oldpos=camera.position;
        Gdx.app.log("Cameraposition__", "X: " + camera.position.x + " Y: " + camera.position.y);
        Gdx.app.log("Bprders__", "XMIN: " + xmin + " XMAX: " + xmax + " YMIN: " + ymin  + " YMAX: " + ymax);
        if(camera.position.x<w/2)
        {
            camera.position.x=w/2;
            if (delta.x>0) {
                camera.translate(0, -delta.y);
            }else{
                camera.translate(-delta.x,delta.y);
            }
        }else if(camera.position.x>=(mapwidth-(w/2))){
                camera.position.x=mapwidth-(w/2);
                if (delta.x<0) {
                    camera.translate(0, delta.y);
                }else{
                    camera.translate(-delta.x,delta.y);
                }
            }
            else{
            camera.translate(-delta.x,0);
        }

        if(camera.position.y<=h/2)
        {
            camera.position.y=h/2;
            if (delta.y>0) {
                camera.translate(-delta.x,0);
            }else{
                camera.translate(-delta.x,delta.y);
            }
        }else if(camera.position.y>=mapheight-(h/2)){
            camera.position.x=mapheight-(h/2);
            if (delta.y<0) {
                camera.translate(-delta.x, 0);
            }else{
                camera.translate(-delta.x,delta.y);
            }
        }
        else{
            camera.translate(0,delta.y);
        }



        /*if (camera.position.x<(w/2)){
            camera.translate(-camera.position.x,0);
        }*/

        camera.translate(-delta.x,delta.y);
        lastTouch = newTouch;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
