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
import java.util.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;

class Globals {
    public static final float YP = 1.0f;
    public static final float YN = 2.0f;
    public static final float XP = 3.0f;
    public static final float XN = 4.0f;
    public static long window = 0;
    public static boolean flag = false;
    public static int size = -1;
    public static List<List<Float>> listOfLists = new ArrayList<List<Float>>();
    public static List<Map> initialInfo = new ArrayList<Map>();
    public static Queue<List<Float>> queue = new LinkedList<List<Float>>();
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

        int id = 0;

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
                Thread t = new ClientHandler(s, dis, dos, id);

                // Invoking the start() method
                t.start();
                id++;
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
    int id;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int id)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.id = id;
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

            Globals.listOfLists.set(id ,new ArrayList<>(Arrays.asList(Float.valueOf(itemInfo[0]),
                                            Float.valueOf(itemInfo[1]), Float.valueOf(itemInfo[2]))));
//            Globals.listOfLists.get(count).set(0,Float.valueOf(itemInfo[0]));
//            Globals.listOfLists.get(count).set(1,Float.valueOf(itemInfo[1]));

            // System.out.println(Float.valueOf(itemInfo[0]) + " " + Float.valueOf(itemInfo[1]));

            if (Globals.initialInfo.get(id).get("name").equals("projectile") && Float.parseFloat(itemInfo[1]) != 0.0f ) {
                Globals.queue.add(new ArrayList<>(Arrays.asList(Float.valueOf(itemInfo[0]), Float.valueOf(itemInfo[1]))));
            }

//            System.out.println(Globals.initialInfo.get(id).get("name"));
//            System.out.println(Globals.initialInfo.get(id).get("speed"));
//            System.out.println(Globals.initialInfo.get(id).get("x"));
//            System.out.println(Globals.initialInfo.get(id).get("y"));
//            System.out.println("");
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
        String[] itemInfo = received.split(",");

        Map m = new LinkedHashMap(itemInfo.length);
        m.put("name", itemInfo[0]);
        m.put("speed", itemInfo[1]);
        m.put("x", Float.valueOf(itemInfo[2]));
        m.put("y", Float.valueOf(itemInfo[3]));

        Globals.listOfLists.add(new ArrayList<>(Arrays.asList(0.0f, 0.0f, 0.0f)));
        Globals.initialInfo.add(m);
        Globals.size++;


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
    float deltaTime = 0.0005f;
    boolean PREDICTION_STATUS = true;

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

    public void square(float x, float y, float dx, float dy) {
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
                render(i);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            glfwSwapBuffers(window); // swap the color buffers
        }
    }

    private void render(int i) {

        float newX = Globals.listOfLists.get(i).get(0);
        float newY = Globals.listOfLists.get(i).get(1);
        float direct = Globals.listOfLists.get(i).get(2);

        if (PREDICTION_STATUS & (Globals.initialInfo.get(i).get("name").equals("projectile"))) {
            System.out.println("Projectile Predict Time " + Globals.queue.size());

            if (Globals.queue.size() == 0) {

                float speed = Float.parseFloat((String) Globals.initialInfo.get(i).get("speed"));

                if (direct == Globals.XP)
                    newX += 40 * speed * deltaTime;
                else if (direct == Globals.XN)
                    newX -= 40 * speed * deltaTime;
                else if (direct == Globals.YP)
                    newY += 40 * speed * deltaTime;
                else if (direct == Globals.YN)
                    newY -= 40 * speed * deltaTime;

                Globals.listOfLists.get(i).set(0, newX);
                Globals.listOfLists.get(i).set(1, newY);

                square(0.05f, 0.05f, newX, newY);

            } else {
                Globals.queue.remove();
                square(0.05f, 0.05f, newX, newY);
            }
        } else {
            square(0.05f, 0.05f, newX, newY);
        }
    }
}