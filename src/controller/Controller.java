package controller;

import inputs.Keyboard;
import inputs.Mouse;
import objectdata.Cube;
import objectdata.Renderable;
import objectdata.Sphere;
import objectdata.Tube;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import view.Canvas;
import view.Scene;


import javax.swing.*;


public class Controller {
    private final Canvas canvas;
    private final Scene scene;
    private final ColorBuffer colorBuffer;
    private final DepthBuffer depthBuffer;
    private final InputManager input;


    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.scene = new Scene();
        this.colorBuffer = (ColorBuffer) canvas.getRaster();
        this.depthBuffer = new DepthBuffer(canvas.getWidth(), canvas.getHeight());


        canvas.setFocusTraversalKeysEnabled(false);
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();

        this.input = new InputManager(new Keyboard(), new Mouse(), scene);
        this.input.injectListeners(canvas);

        Cube c1 = new Cube(0, 0, 10);
        c1.setRotationSpeed(0.02f, 0.03f, 0);
        scene.addObject(c1);

        Cube c2 = new Cube(0, 0, 5);
        c2.setRotationSpeed(0.02f, 0.01f, 0.01f);
        scene.addObject(c2);

        Cube c3 = new Cube(2, 2, 10);
        c3.setRotationSpeed(0.02f, 0.03f, 0);
        scene.addObject(c3);

        Sphere s1 = new Sphere(1,1,1,1,6,6);
        s1.setRotationSpeed(0.02f, 0.03f, 0);
        scene.addObject(s1);

        Tube t1 = new Tube(0,0,0,1,6,6);
        t1.setRotationSpeed(0.02f, 0.03f, 0);
        scene.addObject(t1);

        Timer timer = new Timer(16, e -> redraw());
        timer.start();
    }

    private void redraw() {
        colorBuffer.clear();
        depthBuffer.clear();

        input.processInput(1.0f);

        scene.update(1.0f);

        for (Renderable obj : scene.getObjects()) {
            obj.render(scene.getCamera(), colorBuffer, depthBuffer,
                    canvas.getWidth(), canvas.getHeight(),
                    true, true, true);
        }
        Renderable c1 = scene.getObjects().get(0);
        c1.render(scene.getCamera(), colorBuffer, depthBuffer,
                canvas.getWidth(), canvas.getHeight(),
                false, true, false);

        Renderable s1 = scene.getObjects().get(2);
        s1.render(scene.getCamera(), colorBuffer, depthBuffer,
                canvas.getWidth(), canvas.getHeight(),
                false, true, false);

        Renderable t1 = scene.getObjects().get(3);
        t1.render(scene.getCamera(), colorBuffer, depthBuffer,
                canvas.getWidth(), canvas.getHeight(),
                false, true, false);
        t1.setSelected(true);
        input.tick();
        canvas.repaint();
    }
}
