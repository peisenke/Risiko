package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;


/**
 * Created by Patrick on 14.04.2016.
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {

    TiledMap tiledMap;
    private Stage s;
    InputMultiplexer in = new InputMultiplexer();
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    private Vector2 lastTouch = new Vector2();
    float w = 0;
    float h = 0;
    MyGdxGame g;
    int mapwidth = 0;
    int mapheight = 0;
    private Vector2 pinchopt1 = new Vector2(0, 0);
    private Vector2 pinchopt2 = new Vector2(0, 0);
    private HudLayer hud;
    private PolygonSpriteBatch objectsBatch;
    private GameLogic gl;

    public GameScreen(MyGdxGame g) {
        this.g = g;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        s=new Stage();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        tiledMap = new TmxMapLoader().load("Map/Map.tmx");
        mapwidth = tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);
        mapheight = tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        if(gl==null);
        {
            gl=new GameLogic(this,tiledMap);
        }

        // init Polygons
        objectsBatch =new PolygonSpriteBatch();
        // create new world
        gl.getGs().setWorld(new RisikoWorld(tiledMap));

        hud = new HudLayer(w,h);

        in.addProcessor(new GestureDetector((this)));
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
            objectsBatch.end();
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
    public boolean touchDown(float x, float y, int pointer, int button) {
        lastTouch = new Vector2(x, y);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        Pos pos = new Pos((int)x,(int) y);
        Country c=gl.getGs().getWorld().selectCountry( pos.toAbs(camera));
        Gdx.app.log("Phase:  ", gl.getGs().getPhase());
        if(c!=null) {
            if (gl.getGs().isTurn() == true) {
                if (gl.getGs().getPhase().equals("rein")) {
                    gl.setFirstcntry(c);
                    gl.reinforce(1);
                } else if (gl.getGs().getPhase().equals("att")) {
                    if(gl.getFirstcntry()==null){
                        gl.setFirstcntry(c);
                    }else{
                        gl.setSecondcntry(c);
                    }
                    if(gl.getFirstcntry()!=null && gl.getSecondcntry()!=null){
                        Gdx.app.log("Attack:  ", gl.getFirstcntry().getName() + " --> " + gl.getSecondcntry().getName());
                        setInputProcessorStage();
                        gl.attack();
                    }
                } else if (gl.getGs().getPhase().equals("mov")) {
                    if(gl.getFirstcntry()==null){
                        gl.setFirstcntry(c);
                    }else{
                        gl.setSecondcntry(c);
                    }
                    if(gl.getFirstcntry()!=null && gl.getSecondcntry()!=null){
                        Gdx.app.log("Move:  ", gl.getFirstcntry().getName() + " --> " + gl.getSecondcntry().getName());
                        setInputProcessorStage();
                        gl.move();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        Gdx.app.log("TURN:", "______________TURN CHANGED______________");
        if(gl.getGs().getPhase().equals("rein"))
        {
            gl.getGs().setPhase("att");
        } else if(gl.getGs().getPhase().equals("att"))
        {
            gl.getGs().setPhase("mov");
        } else if(gl.getGs().getPhase().equals("mov"))
        {
            gl.getGs().setPhase("rein");
        }
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector2 newTouch = new Vector2(x, y);
        Vector3 oldpos = camera.position;

        if ((x != -1 && y != -1)&& pinchopt1!=null && pinchopt2!=null) {
            if (pinchopt1.x != 0 || pinchopt2.x != 0 || pinchopt1.y != 0 || pinchopt2.y != 0) {
                if ((pinchopt1.sub(newTouch)).x <= (pinchopt2.sub(newTouch)).x || (pinchopt1.sub(newTouch)).y <= (pinchopt2.sub(newTouch)).y) {
                    lastTouch = pinchopt1;
                    newTouch=lastTouch;
                    pinchopt1=null;
                    pinchopt2=null;
                } else {
                    lastTouch=pinchopt2;
                    newTouch=lastTouch;
                    pinchopt1=null;
                    pinchopt2=null;
                }
            }
        }

        Vector2 delta = newTouch.cpy().sub(lastTouch);
        boolean lockx = false;
        boolean locky = false;
        float xmin = (w / 2) * camera.zoom;
        float xmax = mapwidth - ((w / 2) * camera.zoom);
        float ymin = ((h / 2) * camera.zoom) - hud.getHeigth(); // scroll Menu
        float ymax = mapheight - ((h / 2) * camera.zoom);

        if (x == -1 && y == -1) {
            delta.x=0;
            delta.y=0;
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
                        delta.x = delta.x - (xmin - newpos.x);
                    }
                } else if (newpos.x >= xmax) {
                    if (delta.x <= 0) {
                        delta.x = delta.x - (xmax - newpos.x);
                    }
                }
            }

            if (!locky) {
                if (camera.position.y <= ymin) {
                    if (delta.y <= 0) {
                        delta.y = delta.y + (ymin - newpos.y);
                    }
                } else if (camera.position.y >= ymax) {
                    if (delta.y >= 0) {
                        delta.y = delta.y + (ymax - newpos.y);
                    }
                }
            }

            camera.translate(-delta.x, delta.y);
            lastTouch = newTouch;
        }
        return true;
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
        float distold = initialPointer1.dst2(initialPointer2);
        float distnew = pointer1.dst2(pointer2);

        float dist = (distold - distnew) / camera.zoom;

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
        pinchopt1=new Vector2();
        pinchopt2=new Vector2();
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

    public void setInputProcessorGame(){
        Gdx.input.setInputProcessor(in);
    }


    public void setInputProcessorStage(){
        Gdx.input.setInputProcessor(s);

    }
}