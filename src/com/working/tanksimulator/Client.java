package com.working.tanksimulator;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;


// Client class
public class Client
{
    static String tosend = "";
    static DataInputStream dis = null;
    static DataOutputStream dos = null;
    private static long window;
    static Context item = null;

    public static void main(String[] args) throws IOException
    {
        Scanner scn = new Scanner(System.in);
        float x = 0.0f, y = 0.f;
        Timer timer = new Timer();
        float deltaTime = 0.0005f;
        float displaySize = 0.95f;

        System.out.println("Enter an item");
        tosend = scn.nextLine();
        String toconvert;
        int latency = 0;

        switch (tosend) {
            case "tank" -> {
                item = new Context(new Tank("tank", 1, -displaySize, 0, 2));
                latency = 1;
            }
            case "car" -> {
                item = new Context(new Car("car", 3, displaySize, 0, 5));
                latency = 1;
            }
            case "projectile" -> {
                item = new Context(new Projectile("projectile", 10, -displaySize / 2, 0, 15));
                latency = 3;
            }
            default -> System.out.println("Wrong Command Try Again");
        }
        
        item.printItem();

        try
        {
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            // Initialize Items on screen
            dos.writeUTF(item.itemIdentifier());

            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {
                    try {
                        tosend = item.toString();
                        dos.writeUTF(tosend);
                        System.out.println(tosend);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            timer = new Timer("MyTimer");//create a new Timer

            timer.scheduleAtFixedRate(timerTask, 250, latency * 1000);

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

                if ( glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
                    y += item.getSpeed() * deltaTime;
                    System.out.println("Up");
                }

                if ( glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
                    y -= item.getSpeed() * deltaTime;
                    System.out.println("Down");
                }

                if ( glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
                    x -= item.getSpeed() * deltaTime;
                    System.out.println("Left");
                }

                if ( glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
                    x += item.getSpeed() * deltaTime;
                    System.out.println("Right");
                }

              //  System.out.println(dis.readUTF());
                // toconvert = scn.nextLine();

                // String[] position = toconvert.split(",");
                item.movement(x, y);

                System.out.println(tosend);
                //dos.writeUTF(tosend);



                // If client sends exit,close this connection
                // and then break from the while loop
                if(tosend.equals("Exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }

            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
