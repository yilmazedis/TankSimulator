package com.working.tanksimulator;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.nio.IntBuffer;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class DisplaySimulator {

    private static long window;

    private static void init() {
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
        window = glfwCreateWindow(640, 480, "Hello World!", NULL, NULL);
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
    }

    private static void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.5f, 0.5f, 0.0f);

        float x = 0, y = 0;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {

            if ( glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
                y += 0.001f;
                System.out.println("Up");
            }

            if ( glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
                y -= 0.001f;
                System.out.println("Down");
            }

            if ( glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
                x -= 0.001f;
                System.out.println("Left");
            }

            if ( glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
                x += 0.001f;
                System.out.println("Right");
            }

            glfwPollEvents();
            //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT);

            square(0.1f,0.1f, x, y);

            square(0.1f,0.1f, x + 1f, y + 1f);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            //glfwPollEvents();

        }
    }

    public static void run() {
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

    public static void square(float x,float y, float dx, float dy) {
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

    public static void displayMain() {

        glfwInit();

        long win = glfwCreateWindow(640, 480, "window2", 0, 0);

        //glfwShowWindow(win);


        while(!glfwWindowShouldClose(win)) {

            glfwPollEvents();

            glfwSwapBuffers(win);


            if ( glfwGetKey(win, GLFW_KEY_UP) == GLFW_PRESS) {
                System.out.println("Up");
            }

            if ( glfwGetKey(win, GLFW_KEY_DOWN) == GLFW_PRESS) {
                System.out.println("Down");
            }

            if ( glfwGetKey(win, GLFW_KEY_LEFT) == GLFW_PRESS) {
                System.out.println("Left");
            }

            if ( glfwGetKey(win, GLFW_KEY_RIGHT) == GLFW_PRESS) {
                System.out.println("Right");
            }
        }

    }

    public static void main(String[] args) {

        //displayMain();

         //run();

            init();

//        JTextField component = new JTextField();
//        component.addKeyListener(new MyKeyListener());
//
//        JFrame f = new JFrame();
//
//        f.add(component);
//        f.setTitle("test");
//        f.setSize(320, 240);
//        f.setVisible(true);

        //System.out.println("hello world");
    }
}

//class MyKeyListener extends KeyAdapter {
//    public void keyPressed(KeyEvent evt) {
//        if (evt.getKeyChar() == 'w') {
//            System.out.println("Check for key characters: " + evt.getKeyChar());
//        }
//
//        if (evt.getKeyChar() == 's') {
//            System.out.println("Check for key characters: " + evt.getKeyChar());
//        }
//
//        if (evt.getKeyChar() == 'a') {
//            System.out.println("Check for key characters: " + evt.getKeyChar());
//        }
//
//        if (evt.getKeyChar() == 'd') {
//            System.out.println("Check for key characters: " + evt.getKeyChar());
//        }
//    }
//}
