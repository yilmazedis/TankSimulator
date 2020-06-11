package com.working.tanksimulator;

public class Tank extends AbstractItem implements Item{
    public Tank(String name, float speed, float x, float y, int color) {
        super(name, speed, x, y, color);
    }

    @Override
    public void movement(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void printItem() {
        super.printItem();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String itemIdentifier() {
        return super.itemIdentifier();
    }

    @Override
    public int getColor() {
        return super.getColor();
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    public float getX() {
        return super.getX();
    }

    @Override
    public float getY() {
        return super.getY();
    }

    @Override
    public String getName() {
        return super.getName();
    }
}

