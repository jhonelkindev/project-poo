package model;

import java.awt.*;

public abstract class Entity {
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double speed;

    public Entity(double x, double y, int width, int height, double speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public abstract void update();

    public Rectangle getBounds() {
        int footHeight = height / 3;
        return new Rectangle((int) x, (int) (y + height - footHeight), width, footHeight);
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
