package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by riederch on 25.04.2016.
 * A country is a PolygonSpite extended with an Name, number of troops, its owner and
 * the neighbours
 */
public class Country extends PolygonSprite {
    private final Polygon polygon;
    private String name;
    private int troops;
    private Player owner;
    private boolean cheat;
    private ArrayMap<String, Country> n= new ArrayMap<String, Country>();

    /**
     * Gets the Name of the Country
     * @return String Name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Player owner
     * @return Player owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Get number of troops
     * @return int troops
     */
    public int getTroops() {

        return troops;
    }

    /**
     * Returns the Polygon of the country, used for drawing
     * @return Poygon
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * Creates a new Country by using name and Polygon
     *
     * @param name The Name of the country
     * @param polygon  The Polygon from the tiled map
     */
    public Country(String name, Polygon polygon) {
        // creates an polygonRegion from the Polygon
        super(calcul(polygon));
        this.polygon=polygon;
		
        // set Position of the Polygon
        setOrigin(polygon.getOriginX(),polygon.getOriginY());
        setPosition(polygon.getX(),polygon.getY());

        this.name = name;
        this.troops = 0;
        this.cheat=false;
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
        pix.setColor(0xDEADBEF0); // DE is red, AD is green and BE is blue.
        pix.fill();
        Texture textureSolid = new Texture(pix);

        return new PolygonRegion(new TextureRegion(textureSolid),polygon.getVertices(),triangles);
    }


    /**
     * Change number of troops
     * if troops are 0 then change owner of the country
     * @param i int of troops
     */
    public void changeTroops(int i) {
        if(this.troops==0){
            this.setColor(owner.getC());
        }
        troops = getTroops() + i;

    }

    /**
     * Set troops of the country
     * Use with caution
     * @param troops
     */
    public void setTroops(int troops) {
        this.troops = troops;
        if (this.troops<0){
            this.troops=0;
        }
    }

    /**
     * Set the Owner
     * @param owner of country
     */
    public void setOwner(Player owner) {
        this.owner = owner;
        this.setColor(owner.getC());
    }

    /**
     *  Get neighbours of the country
     * @return array map
     */
    public ArrayMap<String, Country> getN() {
        return n;
    }

    public void setN(ArrayMap<String, Country> n) {
        this.n = n;
    }

    public boolean isCheat() {
        return cheat;
    }

    public void setCheat(boolean cheat) {
        this.cheat = cheat;
    }
}