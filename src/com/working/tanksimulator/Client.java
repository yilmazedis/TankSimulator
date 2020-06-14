package com.working.tanksimulator;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

// Client class
public class Client
{
    static String tosend = "";
    static DataOutputStream dos = null;
    static Context item = null;
    static Float direct = 0.0f;
    static boolean deal = false;

    public static void main(String[] args)
    {
        Scanner scn = new Scanner(System.in);
        float x, y;
        Timer timer;
        float deltaTime = 0.0005f;
        float displaySize = 0.95f;
        int latency = 0;


        System.out.println("Enter an item");
        tosend = scn.nextLine();

        boolean checkItem = true;
        while (checkItem) {
            switch (tosend) {
                case "tank" -> {
                    item = new Context(new Tank("tank", 1, -displaySize, 0, 2));
                    latency = 1;
                    checkItem = false;
                }
                case "car" -> {
                    item = new Context(new Car("car", 3, displaySize, 0, 5));
                    latency = 1;
                    checkItem = false;
                }
                case "projectile" -> {
                    item = new Context(new Projectile("projectile", 10, -displaySize / 2, 0, 15));
                    latency = 3;
                    checkItem = false;
                }
                default -> {
                    System.out.println("Wrong Command Try Again");
                    System.out.println("Enter an item");
                    tosend = scn.nextLine();
                }
            }
        }
        
        item.printItem();

        try
        {
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams
            dos = new DataOutputStream(s.getOutputStream());
            // Initialize Items on screen
            dos.writeUTF(item.itemIdentifier());

            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {

                    try {
                        if (deal)
                            tosend = item.toString() + "," + direct.toString();
                        else
                            tosend = item.toString() + "," + 0.0f;

                        deal = false;
                        System.out.println(tosend);
                        dos.writeUTF(tosend);
                        System.out.println(tosend);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            timer = new Timer("MyTimer");//create a new Timer

            timer.scheduleAtFixedRate(timerTask, 200, latency * 1000);

            glfwInit();
            long window = glfwCreateWindow(320, 240, item.getName(), 0, 0);

            x = item.getX();
            y = item.getY();

            while(!glfwWindowShouldClose(window)) {

                glfwPollEvents();

                glfwSwapBuffers(window);

                if ( glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
                    x = item.getX();
                    y = item.getY();
                    System.out.println("Position Reset");
                }

                // Arrow Keys
                if ( glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
                    y += item.getSpeed() * deltaTime;
                    direct = Globals.YP;
                    System.out.println("Up");
                    deal = true;
                }

                if ( glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
                    y -= item.getSpeed() * deltaTime;
                    direct = Globals.YN;
                    System.out.println("Down");
                    deal = true;
                }

                if ( glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
                    x -= item.getSpeed() * deltaTime;
                    direct = Globals.XN;
                    System.out.println("Left");
                    deal = true;
                }

                if ( glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
                    direct = Globals.XP;
                    x += item.getSpeed() * deltaTime;
                    System.out.println("Right");
                    deal = true;
                }

                item.movement(x, y);

                //System.out.println(tosend);
            }

            // closing resources
            scn.close();
            s.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
