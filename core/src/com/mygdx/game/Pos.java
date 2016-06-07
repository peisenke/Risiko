package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by riederch on 02.05.2016.
 * Position consists of X and Y Coordinate
 */

public class Pos {
    private double x;
    private double y;

    /**
     * Creates an new Object wit x and Y
     * @param x2 X Position
     * @param y2 YPosition
     */
    public Pos(float x2, float y2) {
        super();
        this.x = x2;
        this.y = y2;
    }

    /**
     * Get xPosition
     * @return position
     */
    public double getX() {
        return x;
    }

    /**
     * Set xPosition
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get Y Position
     * @return yPosition
     */
    public double getY() {
        return y;
    }

    /**
     * Set Y Position
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Convert Position to the absolute position of the world
     * @param camera Used Camera
     * @return Apsolute Position
     */
    public Pos toAbs(Camera camera) {
        Vector3 clickCoordinates = new Vector3((float)x, (float)y, 0);
        Vector3 position = camera.unproject(clickCoordinates);

        return new Pos(position.x, position.y);
    }

    /**
     * Calculates the new Position of the Object according with the speed
     * > used in an older Project
     *
     * @param speed of the object
     * @param target Position
     * @return the Target position for further use
     */
    public Pos moveTo(int speed, Pos target) {
        boolean re = target.x > x;
        double length = Math.sqrt(Math.pow(target.getX() - this.x, 2)
                + Math.pow(target.getY() - this.y, 2));
        double angle = Math.asin((target.getY() - this.y) / length);
        if (!re)
            angle = -angle;
        if (speed > length) {
            target = null;
            speed = (int) length;
        }

        double yn = Math.sin(angle) * speed;
        double xn = Math.sqrt(Math.pow(speed, 2) - Math.pow(yn, 2));
        if (re) {
            y = (float) (yn + y);
            x = (float) (xn + x);
        } else {
            y = (float) (y - yn);
            x = (float) (x - xn);
        }
        return target;
    }

    /**
     * Writes the Position for debugging
     * @return Position string
     */
    @Override
    public String toString() {
        return "pos: " + x + "/" + y;
    }

    /**
     * Get distance to an other Point
     * @param target Position
     * @return distance
     */
    public int getDist(Pos target) {
        return (int) Math.sqrt(Math.pow(target.getX() - this.x, 2)
                + Math.pow(target.getY() - this.y, 2));
    }
}