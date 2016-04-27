package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;

/**
 * Created by riederch on 25.04.2016.
 *
 */
public class Country extends PolygonSprite {
    private String name;
    private int troops;
    private int owner;

    /**
     * Creates a new Country by using:
     *
     * @param name The Name of the country
     * @param polygon  The Polygon from the tiled map
     */
    public Country(String name, Polygon polygon) {
        super(calcul(polygon));

        setOrigin(polygon.getOriginX(),polygon.getOriginY());
        setPosition(polygon.getX(),polygon.getY());
        this.name = name;
        this.troops = 0;
        this.owner = 0;
    }

    /**
     * Creates an polygon region from an polygon
     *
     * @param polygon Polygon from the map
     * @return Polygon region can be drawed
     */
    private static PolygonRegion calcul(Polygon polygon) {

        EarClippingTriangulator ect=new EarClippingTriangulator();
        short[] triangles=ect.computeTriangles(polygon.getVertices()).toArray();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0xDEADBEFF); // DE is red, AD is green and BE is blue.
        pix.fill();
        Texture textureSolid = new Texture(pix);

        return new PolygonRegion(new TextureRegion(textureSolid),polygon.getVertices(),triangles);
    }


}