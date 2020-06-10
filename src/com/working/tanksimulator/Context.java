package com.working.tanksimulator;

public class Context  {

    private Item item;

    public Context(Item item){
        this.item = item;
    }

    public void movement(int x, int y) {
        item.movement(x,y);
    }

    public void printItem() {
        item.printItem();
    }

    @Override
    public String toString() {
        return item.toString();
    }

    public String itemIdentifier() {
        return item.itemIdentifier();
    }
}
