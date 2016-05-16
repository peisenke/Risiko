package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by riederch on 25.04.2016.
 * <p/>
 * A World contains countries
 */

public class RisikoWorld {
    private ArrayMap<String, Country> countries;
    private Player[] players;
    private BitmapFont bf;

    /**
     * creates a new Risiko world from an tiled map
     *
     * @param tiledMap
     */
    public RisikoWorld(TiledMap tiledMap) {
        // get from Layer named "Laender" all Objects
        MapObjects objects = tiledMap.getLayers().get("Laender").getObjects();
        this.countries = new ArrayMap<String, Country>();
        bf = new BitmapFont();
        // create Countries and give them a name and a polygon shape
        int i = 0;
        for (MapObject object : objects) {
            if (object instanceof PolygonMapObject) {

                String name = (String) object.getProperties().get("Land");
                Polygon poly = ((PolygonMapObject) object).getPolygon();

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
            bf.draw(batch,
                    country.value.getName() +
                            "\n Owner: " + country.value.getOwner() + "" +
                            "\nTruppen: " + country.value.getTroops(),
                    rct.getX() + rct.getWidth() / 2, rct.getY() + rct.getHeight() / 2);


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


}