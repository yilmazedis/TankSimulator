package com.working.tanksimulator;

public class DisplaySimulator {
    public static void main(String[] args) {

        Context tank = new Context(new Tank("tank", 3,0,0,10));
        Context projectile = new Context(new Projectile("projectile", 3,0,0,10));
        Context car = new Context(new Car("car", 3,0,0,10));

        tank.printItem();
        tank.movement(10,4);
        tank.printItem();

        System.out.println("hello world");
    }
}
