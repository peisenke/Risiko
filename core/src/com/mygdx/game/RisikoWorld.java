package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by riederch on 25.04.2016.
 *
 * A World contains countries
 */

public class RisikoWorld {
    private Country[] countries;

    /**
     * creates a new Risiko world from an tiled map
     *
     *
     * @param tiledMap
     */
    public RisikoWorld(TiledMap tiledMap) {
        // get from Layer named 2 all Objects
        MapObjects objects = tiledMap.getLayers().get("Laender").getObjects();
        this.countries=new Country[objects.getCount()];

        // create Countries and give them a name and a polygon shape
        int i=0;
        for (MapObject object : objects) {
            if (object instanceof PolygonMapObject) {

                String name = object.getName();
                Polygon poly = ((PolygonMapObject) object).getPolygon();

                countries[i] = new Country(name, poly);
                i++;
            }
        }
    }

    /**
     * Draws all countries to an PolygonSpriteBatch
     * @param batch
     */
    public void draw(PolygonSpriteBatch batch) {
        for (Country country:countries ) {
            country.draw(batch);
        }
    }
}
