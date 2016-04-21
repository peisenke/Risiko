package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Patrick on 14.04.2016.
 */
public class GameScreen implements Screen, InputProcessor, GestureDetector.GestureListener {
    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    private Vector2 lastTouch = new Vector2();
    private ShapeRenderer sr;
    float w = 0;
    float h = 0;
    Game g;
    int mapwidth = 0;
    int mapheight = 0;


    public GameScreen(Game g) {
        this.g = g;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        tiledMap = new TmxMapLoader().load("Map/Risiko-map.tmx");
        mapwidth = tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);
        mapheight = tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        InputMultiplexer in = new InputMultiplexer();
        in.addProcessor(new GestureDetector(this));
        in.addProcessor(this);
        Gdx.input.setInputProcessor(in);

        sr = new ShapeRenderer();
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
        w = width;
        h = height;
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
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 newTouch = new Vector2(screenX, screenY);
        Vector2 delta = newTouch.cpy().sub(lastTouch);
        boolean lockx = false;
        boolean locky = false;

        if (screenX == -1 && screenY == -1) {
            delta.x = 0;
            delta.y = 0;
            newTouch = lastTouch;
        }

        if (pointer == 0 && Math.abs(delta.x) < 100 && Math.abs(delta.y) < 100) {
            float xmin = (w / 2) * camera.zoom;
            float xmax = mapwidth - ((w / 2) * camera.zoom);
            float ymin = (h / 2) * camera.zoom;
            float ymax = mapheight - ((h / 2) * camera.zoom);

            Vector3 oldpos = camera.position;

            if (ymin >= ymax) {
                locky = true;
                delta.y = (mapheight / 2) - camera.position.y;
            }
            if (xmin >= xmax) {
                lockx = true;
                delta.x = -((mapwidth / 2) - camera.position.x);
            }

            Vector3 newpos = new Vector3(oldpos.x - delta.x, oldpos.y + delta.y, 0);

            if (lockx == false) {
                if (newpos.x <= xmin) {
                    if (delta.x > 0) {
                        delta.x = delta.x - (xmin - newpos.x);
                    }
                } else if (newpos.x >= xmax) {
                    if (delta.x < 0) {
                        delta.x = delta.x - (xmax - newpos.x);
                    }
                }
            }

            if (locky == false) {
                if (camera.position.y <= ymin) {
                    if (delta.y < 0) {
                        delta.y = delta.y + (ymin - newpos.y);
                    }
                } else if (camera.position.y >= ymax) {
                    if (delta.y > 0) {
                        delta.y = delta.y + (ymax - newpos.y);
                    }
                }
            }
        }
        camera.translate(-delta.x, delta.y);
        newTouch.x=screenX;
        newTouch.y=screenY;
        lastTouch = newTouch;
        return true;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        float dist = (initialDistance - distance) / (camera.zoom);
        while (Math.abs(dist) > 0.05) {
            dist = dist / 5;
        }
        if ((camera.zoom + dist) < 1) {
            camera.zoom = 1;
        } else if ((camera.zoom + dist) > 5) {
            camera.zoom = 5;
        } else {
            camera.zoom = camera.zoom + dist;
        }
        touchDragged(-1, -1, 0);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
