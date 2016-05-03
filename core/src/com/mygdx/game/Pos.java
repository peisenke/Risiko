package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by riederch on 02.05.2016.
 */

public class Pos {
    private float x;
    private float y;

    public Pos(float x2, float y2) {
        super();
        this.x = x2;
        this.y = y2;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Pos toAbs(Camera camera) {
        Vector3 clickCoordinates = new Vector3(x, y, 0);
        Vector3 position = camera.unproject(clickCoordinates);

        return new Pos(position.x, position.y);
    }

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

    @Override
    public String toString() {
        return "pos: " + x + "/" + y;
    }

    public int getDist(Pos target) {
        return (int) Math.sqrt(Math.pow(target.getX() - this.x, 2)
                + Math.pow(target.getY() - this.y, 2));
    }
}