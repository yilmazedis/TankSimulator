package com.working.tanksimulator;

public class Projectile extends AbstractItem implements Item{
    public Projectile(String name, int speed, int x, int y, int color) {
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

    public int getSpeed() {
        return speed;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
