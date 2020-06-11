package com.working.tanksimulator;

public abstract class AbstractItem {

    protected String name;
    protected int speed;
    protected int x, y;
    protected int color;
    private Item item;

    public AbstractItem(String name, int speed, int x, int y, int color) {
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