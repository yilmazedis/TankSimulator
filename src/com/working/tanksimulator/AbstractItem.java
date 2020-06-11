package com.working.tanksimulator;

public abstract class AbstractItem {

    protected String name;
    protected float speed;
    protected float x, y;
    protected int color;
    private Item item;

    public AbstractItem(String name, float speed, float x, float y, int color) {
        this.name = name;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void printItem() {
        System.out.println("Name " + name + " Speed " + speed + " Position " + x + "," + y + " Color " + color);
    }

    @Override
    public String toString() {
        return  x +
                "," +
                y;
    }

    public String itemIdentifier() {
        return  name + "," +  speed + "," + x + "," + y + "," + color;
    }

    public float getSpeed() {
        return speed;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}