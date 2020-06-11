package com.working.tanksimulator;

public class Tank extends AbstractItem implements Item{
    public Tank(String name, int speed, int x, int y, int color) {
        super(name, speed, x, y, color);
    }

    @Override
    public void movement(int x, int y) {
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
    public int getSpeed() {
        return super.getSpeed();
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public String getName() {
        return super.getName();
    }
}

