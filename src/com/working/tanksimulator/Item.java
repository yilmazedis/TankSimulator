package com.working.tanksimulator;

public interface Item {
    void movement(int x, int y);
    void printItem();
    String itemIdentifier();
}