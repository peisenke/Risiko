package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Iterator;


/**
 * Created by Patrick on 14.04.2016.
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {

    private TiledMap tiledMap;
    private Stage s;
    private InputMultiplexer in = new InputMultiplexer();
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private Vector2 lastTouch = new Vector2();
    private double w;
    private double h;
    private MyGdxGame g;
    private int mapwidth;
    private int mapheight;
    private Vector2 pinchopt1 = new Vector2(0, 0);
    private Vector2 pinchopt2 = new Vector2(0, 0);
    private HudLayer hud;
    private PolygonSpriteBatch objectsBatch;
    private GameLogic gl;
    private boolean initialized = false;

    public GameScreen(MyGdxGame g) {
        Gdx.app.log("Game Screen", "Konstruktor Game Screen");
        this.g = g;
        tiledMap = new TmxMapLoader().load("Map/Map.tmx");
        mapwidth = tiledMap.getProperties().get("width", Integer.class)
                * tiledMap.getProperties().get("tilewidth", Integer.class);
        mapheight = tiledMap.getProperties().get("height", Integer.class)
                * tiledMap.getProperties().get("tileheight", Integer.class);
        if (gl == null) {
            gl = new GameLogic(this, tiledMap);
        }

        for (Player p : g.getmNC().getmRemotePeerEndpoints()) {
            String msg = "0;" + p.getId();
            g.getmNC().sendMessage(p.getEndpointID(), msg.getBytes());
        }

       // gl.getGs().setWorld(new RisikoWorld(tiledMap, g));

        if(g.getmNC().ismIsHost()) {
            Iterator<ObjectMap.Entry<String, Country>> i = gl.getGs().getWorld().getCountries().iterator();
            String str = "";
            while (i.hasNext()) {
                ObjectMap.Entry<String, Country> x = i.next();
                if (x.value.getOwner() != null) {
                    str = "1;" + x.value.getName() + ";" + x.value.getTroops() +
                            ";" + x.value.getOwner().getId() + ";" + x.value.getOwner().getName();
                    Gdx.app.log("WWWW", str);
                    g.getmNC().sendMessage(str.getBytes());
                }
            }
        }
        initialized = true;
        Gdx.app.log("Game Screen", "Konstruktor Game Screen fertigs");
    }

    @Override
    public void show() {
        Gdx.app.log("Game Screen.Show", "im Show von GameScreen");
        camera = new OrthographicCamera();
        s = new Stage();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        camera.setToOrtho(false, (float)w, (float)h);
        camera.update();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // init Polygons
        objectsBatch = new PolygonSpriteBatch();
        hud = new HudLayer((float)w, (float)h,gl);

        in.addProcessor(new GestureDetector(this));
        in.addProcessor(hud.getStage());
        Gdx.input.setInputProcessor(in);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();


        // combine drawed sprites to the map
        objectsBatch.setProjectionMatrix(camera.combined);
        objectsBatch.begin();
        gl.getGs().getWorld().draw(objectsBatch);
        try {
            objectsBatch.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
        s.act(delta);
        s.draw();
        hud.draw(delta);
    }

    @Override
    public void resize(int width, int height) {
        w = width;
        h = height;
    }

    @Override
    public void pause() {
        //No Use at the Moment

    }

    @Override
    public void resume() {
        //No Use at the Moment
    }

    @Override
    public void hide() {
        //No Use at the Moment
    }

    @Override
    public void dispose() {
        //No Use at the Moment
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        lastTouch = new Vector2(x, y);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("TEST", x + "__" + y + "||||||" + (y>hud.getHeigth() && y<h-hud.getHeigth()));
        if (y>hud.getHeigth() && y<h-hud.getHeigth()) {
            Pos pos = new Pos((int) x, (int) y);
            Country c = gl.getGs().getWorld().selectCountry(pos.toAbs(camera));
            Gdx.app.log("Phase:  ", gl.getGs().getPhase());
            if (c != null) {
                if (gl.getGs().isTurn() == true && "rein".equals(gl.getGs().getPhase())) {
                    gl.setFirstcntry(c);
                    gl.reinforce(1);
                } else if (gl.getGs().isTurn() == true && "att".equals(gl.getGs().getPhase())) {
                    if (gl.getFirstcntry() == null) {
                        gl.setFirstcntry(c);
                    } else {
                        gl.setSecondcntry(c);
                    }
                    if (gl.getFirstcntry() != null && gl.getSecondcntry() != null) {
                        Gdx.app.log("Attack:  ", gl.getFirstcntry().getName() + " --> " + gl.getSecondcntry().getName());
                        setInputProcessorStage();
                        gl.attack();
                    }
                } else if (gl.getGs().isTurn() == true && "mov".equals(gl.getGs().getPhase())) {
                    if (gl.getFirstcntry() == null) {
                        gl.setFirstcntry(c);
                    } else {
                        gl.setSecondcntry(c);
                    }
                    if (gl.getFirstcntry() != null && gl.getSecondcntry() != null) {
                        setInputProcessorStage();
                        gl.move();
                    }
                }
            }
        }
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
        Vector2 newTouch = new Vector2(x, y);
        Vector3 oldpos = camera.position;

        if (((int)x != -1 && (int)y != -1) && pinchopt1 != null && pinchopt2 != null) {
            if (pinchopt1.x != 0 || pinchopt2.x != 0 || pinchopt1.y != 0 || pinchopt2.y != 0) {
                if ((pinchopt1.sub(newTouch)).x <= (pinchopt2.sub(newTouch)).x || (pinchopt1.sub(newTouch)).y <= (pinchopt2.sub(newTouch)).y) {
                    lastTouch = pinchopt1;
                    newTouch = lastTouch;
                    pinchopt1 = null;
                    pinchopt2 = null;
                } else {
                    lastTouch = pinchopt2;
                    newTouch = lastTouch;
                    pinchopt1 = null;
                    pinchopt2 = null;
                }
            }
        }

        Vector2 delta = newTouch.cpy().sub(lastTouch);
        boolean lockx = false;
        boolean locky = false;
        double xmin = (w / 2) * camera.zoom;
        double xmax = mapwidth - ((w / 2) * camera.zoom);
        double ymin = ((h / 2) * camera.zoom) - hud.getHeigth()*camera.zoom; // scroll Menu
        double ymax = (mapheight - ((h / 2) * camera.zoom)) + hud.getHeigth()*camera.zoom;

        if ((int)x == -1 && (int)y == -1) {
            delta.x = 0;
            delta.y = 0;
        }

        if (Math.abs(delta.x) < 200 && Math.abs(delta.y) < 200) {

            Vector3 newpos = new Vector3(oldpos.x - delta.x, oldpos.y + delta.y, 0);

            if (ymin >= ymax) {
                locky = true;
                delta.y = (mapheight / 2) - oldpos.y;
            }
            if (xmin >= xmax) {
                lockx = true;
                delta.x = -((mapwidth / 2) - oldpos.x);
            }

            if (!lockx) {
                if (newpos.x <= xmin) {
                    if (delta.x >= 0) {
                        delta.x = (float) (delta.x - (xmin - newpos.x));
                    }
                } else if (newpos.x >= xmax) {
                    if (delta.x <= 0) {
                        delta.x = (float) (delta.x - (xmax - newpos.x));
                    }
                }
            }

            if (!locky) {
                if (camera.position.y <= ymin) {
                    if (delta.y <= 0) {
                        delta.y = (float) (delta.y + (ymin - newpos.y));
                    }
                } else if (camera.position.y >= ymax) {
                    if (delta.y >= 0) {
                        delta.y = (float) (delta.y + (ymax - newpos.y));
                    }
                }
            }

            camera.translate(-delta.x, delta.y);
            lastTouch = newTouch;
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        double distold = initialPointer1.dst2(initialPointer2);
        double distnew = pointer1.dst2(pointer2);

        double dist = (distold - distnew) / camera.zoom;

        while (Math.abs(dist) > 0.05) {
            dist = dist / 5;
        }
        if ((camera.zoom + dist) < 1) {
            camera.zoom = 1;
        } else if ((camera.zoom + dist) > 5) {
            camera.zoom = 5;
        } else {
            camera.zoom = (float) (camera.zoom + dist);
        }
        pinchopt1 = new Vector2();
        pinchopt2 = new Vector2();
        pinchopt1 = pointer1;
        pinchopt2 = pointer2;
        pan(-1, -1, 0, 0);
        return true;
    }

    public Stage getS() {
        return s;
    }

    public void setS(Stage s) {
        this.s = s;
    }

    public void setInputProcessorGame() {
        Gdx.input.setInputProcessor(in);
    }


    public void setInputProcessorStage() {
        Gdx.input.setInputProcessor(s);
    }


    public MyGdxGame getG() {
        return g;
    }

    public GameLogic getGl() {
        return gl;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}