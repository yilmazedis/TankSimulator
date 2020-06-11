package com.working.tanksimulator;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


// Client class
public class Client
{
    static String tosend = "";
    static DataInputStream dis = null;
    static DataOutputStream dos = null;

    public static void main(String[] args) throws IOException
    {
        Scanner scn = new Scanner(System.in);
        Context item = null;
        int x = 0, y = 0;
        Timer timer = new Timer();

        System.out.println("Enter an item");
        tosend = scn.nextLine();
        String toconvert;
        int latency = 0;

        switch (tosend) {
            case "tank":
                item = new Context(new Tank("tank", 1,0,0,2));
                latency = 1;
                break;

            case "car":
                item = new Context(new Car("car", 3,5,0,5));
                latency = 1;
                break;

            case "projectile":
                item = new Context(new Projectile("projectile", 10,25,0,15));
                latency = 3;
                break;
            default:
                System.out.println("Wrong Command Try Again");
                break;
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

            // the following loop performs the exchange of
            // information between client and client handler

            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {
                    try {

                        dos.writeUTF(tosend);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            timer = new Timer("MyTimer");//create a new Timer

            timer.scheduleAtFixedRate(timerTask, 250, latency * 1000);

            while (true)
            {




              //  System.out.println(dis.readUTF());
                // toconvert = scn.nextLine();

                // String[] position = toconvert.split(",");
                item.movement(x++, y++);

                tosend = item.toString();
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

                // printing date or time as requested by client
                //String received = dis.readUTF();
                //System.out.println(received);
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
