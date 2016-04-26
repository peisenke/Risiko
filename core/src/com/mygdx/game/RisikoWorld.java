package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by riederch on 25.04.2016.
 */

public class RisikoWorld {
    private Country[] countries;
    // The pixels per tile. If your tiles are 16x16, this is set to 16f
    private static float ppt = 0;

    public RisikoWorld(TiledMap tiledMap) {

        MapObjects objects = tiledMap.getLayers().get("2").getObjects();
        this.countries=new Country[objects.getCount()];
        System.out.println("world has "+this.countries.length+" countries");
        int i=0;
        for (MapObject object : objects) {
            Shape shape;


            if (object instanceof PolygonMapObject) {
                System.out.println("is a polygon");
                countries[i]=new Country(object.getName(),((PolygonMapObject) object).getPolygon());
                i++;
            }
            else
                System.out.println("no Polygon");;


        }

    }

    public void draw(PolygonSpriteBatch batch) {
        for (Country co:countries ) {
            System.out.println("draw country");
            co.draw(batch);
        }
    }
}
