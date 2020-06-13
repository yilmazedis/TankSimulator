package com.working.tanksimulator;

// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.*;
import java.nio.IntBuffer;
import java.text.*;
import java.util.*;
import java.net.*;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;

class Globals {
    public static long window = 0;
    public static boolean flag = false;
    public static int size = -1;
    public static List<List<Float>> listOfLists = new ArrayList<List<Float>>();

}

// Server class
public class Server
{

    public static void main(String[] args) throws IOException
    {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // create a new thread object
        Thread t_lwjgl = new MY_LWJGL(800, 600);

        // Invoking the start() method
        t_lwjgl.start();

        int count = 0;

        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos, count);

                // Invoking the start() method
                t.start();
                count++;
            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    String received = null;
    Timer timer;
    int latency = 1;
    int count;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int count)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.count = count;
    }

    public void loop() {

        while (true) {
            try {

                // receive the answer from client
                received = dis.readUTF();

            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] itemInfo = received.split(",");

            Globals.listOfLists.set(count ,new ArrayList<>(Arrays.asList(Float.valueOf(itemInfo[0]), Float.valueOf(itemInfo[1]))));
//            Globals.listOfLists.get(count).set(0,Float.valueOf(itemInfo[0]));
//            Globals.listOfLists.get(count).set(1,Float.valueOf(itemInfo[1]));

            // System.out.println(Float.valueOf(itemInfo[0]) + " " + Float.valueOf(itemInfo[1]));
        }
    }

    @Override
    public void run()
    {
        try {
            received = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(received);

        Globals.listOfLists.add(new ArrayList<>(Arrays.asList(0.0f, 0.0f)));
        Globals.size++;

        //itemInfo = received.split(",");


//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println(itemInfo[0] + ": " + received);
//            }
//        };
//        timer = new Timer("MyTimer");//create a new Timer
//        timer.scheduleAtFixedRate(timerTask, 250, latency * 1000);

        loop();

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

class MY_LWJGL extends Thread{

    public long window;
    public int width, height;

    public MY_LWJGL(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public long init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        return window;
    }

    public static void square(float x, float y, float dx, float dy) {
        glBegin(GL_QUADS);
        glColor4f(1.0F, 0.0F, 0.F, 0);
        glVertex2f(-x + dx, y + dy);
        glColor4f(0.0F, 1.0F, 0.F, 0);
        glVertex2f(x + dx, y + dy);
        glColor4f(0.0F, 0.0F, 1.F, 0);
        glVertex2f(x + dx, -y + dy);
        glColor4f(1.0F, 1.0F, 1.F, 0);
        glVertex2f(-x + dx, -y + dy);
        glEnd();
    }

    @Override
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void loop() {

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.5f, 0.5f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {

            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            for (int i = 0; i <= Globals.size; i++)
                MY_LWJGL.square(0.1f, 0.1f, Globals.listOfLists.get(i).get(0), Globals.listOfLists.get(i).get(1));

            glfwSwapBuffers(window); // swap the color buffers
        }
    }

}
