package com.working.tanksimulator;

public interface Item {
    void movement(float x, float y);
    void printItem();
    String itemIdentifier();
    float getSpeed();
    int getColor();
    String getName();
    float getX();
    float getY();
}