package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by riederch on 25.04.2016.
 * <p/>
 * A World contains countries
 */

public class RisikoWorld implements Serializable{
    private ArrayMap<String, Country> countries;
    BitmapFont bf = new BitmapFont();
    /**
     * creates a new Risiko world from an tiled map
     *
     * @param tiledMap
     */
    public RisikoWorld(TiledMap tiledMap,MyGdxGame g) {
        Gdx.app.log("RisikoWorld", "Konstruktor RisikoWorld");

        // get from Layer named "Laender" all Objects
        MapObjects objects = tiledMap.getLayers().get("Laender").getObjects();
        this.countries = new ArrayMap<String, Country>();
        // create Countries and give them a name and a polygon shape
        int i = 0;


        for (MapObject object : objects) {
            if (object instanceof PolygonMapObject) {

                String name = (String) object.getProperties().get("Land");
                Polygon poly = ((PolygonMapObject) object).getPolygon();
                //TODO only at start for server at gamestart
                countries.put(name, new Country(name, poly));
                i++;
            }
        }

        for (ObjectMap.Entry<String, Country> c : countries) {
            if (c.value instanceof Country) {
                for (MapObject object : objects) {
                    if (object instanceof PolygonMapObject) {
                        String name = (String) object.getProperties().get("Land");
                        Polygon poly = ((PolygonMapObject) object).getPolygon();
                        if (object.getProperties().get("Land").equals(c.value.getName())) {
                            String strn = (String) object.getProperties().get("Nachbarn");
                            String[] spl = strn.split(";");
                            for (String s : spl) {
                                Gdx.app.log("T", c.value.getName() + " Nachbar ist " + s);
                                c.value.getN().put(countries.get(s).getName(), countries.get(s));
                            }
                        }
                    }
                }
            }
        }

        //TODO Server initialize

        if (g.getmNC().ismIsHost()) {
            ArrayList<Player> players = g.getmNC().getmRemotePeerEndpoints();
                        int pn = players.size() + 1;

            Iterator<Player> iit = players.iterator();

            Gdx.app.log("NNNN", players.get(0).getId() + "");
            Gdx.app.log("PLAYERS", pn + "");

            int[] cnt = new int[pn];

            int amount = objects.getCount() / pn;
            int tot = 0;
            for (ObjectMap.Entry<String, Country> c : countries) {
                if (c.value instanceof Country) {
                    if (tot < amount * pn) {
                        int curr = MathUtils.random(1, pn);
                        while (cnt[curr - 1] == amount) {
                            curr = MathUtils.random(1, pn);
                        }
                        cnt[curr - 1]++;
                        if (curr == 1) {
                            //TODO Only for Test
                            c.value.setOwner(g.getP());
                            c.value.setTroops(1);
                        } else {
                            c.value.setOwner(players.get(curr - 2));
                            c.value.setTroops(1);
                        }

                    } else {
                        int curr = MathUtils.random(1, pn);
                        cnt[curr - 1]++;
                        if (curr == 1) {
                            //TODO Only for Test
                            c.value.setOwner(g.getP());
                            c.value.setTroops(1);
                        } else {
                            c.value.setOwner(players.get(curr - 2));
                            c.value.setTroops(1);
                        }
                    }
                    tot++;
                }
            }
        } else {
            /*for (ObjectMap.Entry<String, Country> c : countries) {
                if (c.value instanceof Country) {
                    c.value.setOwner(new Player(1, "s", "Ss"));
                }
            }*/
        }
    }

    /**
     * Draws all countries to an PolygonSpriteBatch
     *
     * @param batch
     */
    public void draw(PolygonSpriteBatch batch) {
        for (ObjectMap.Entry<String, Country> country : countries) {
            country.value.draw(batch);
            Rectangle rct = country.value.getBoundingRectangle();
            if(country.value.getOwner()!=null){

            bf.draw(batch,
                    country.value.getName() +
                            "\n Owner: " + country.value.getOwner().getName() + "" +
                            "\nTruppen: " + country.value.getTroops(),
                    rct.getX() + rct.getWidth() / 2, rct.getY() + rct.getHeight() / 2);


        }
        }
    }

    public Country selectCountry(Pos pos) {
        boolean select = false;
        Country c = null;
        for (ObjectMap.Entry<String, Country> country : countries) {
            if (select == false)
                if (country.value.getPolygon().contains((float) pos.getX(), (float) pos.getY())) {
                    Gdx.app.log("Country: ", country.value.getName());
                    select = true;
                    c = country.value;
                }
        }
        return c;
    }

    public ArrayMap<String, Country> getCountries() {
        return countries;
    }
}