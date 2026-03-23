package controller;

import attributes.Attribute;
import attributes.BasicAttribute;
import attributes.TransformAttributes;
import attributes.specific.CubeAttributes;
import attributes.specific.SphereAttributes;
import inputs.Keyboard;
import inputs.Mouse;
import math.Quaternion;
import objectdata.*;
import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import view.Canvas;
import view.HUDPanel;
import view.Scene;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Controller {
    private final Canvas canvas;
    private final Scene scene;
    private final ColorBuffer colorBuffer;
    private final DepthBuffer depthBuffer;
    private final InputManager input;
    private final HUDPanel hud;

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.scene = new Scene();
        this.colorBuffer = (ColorBuffer) canvas.getRaster();
        this.depthBuffer = new DepthBuffer(canvas.getWidth(), canvas.getHeight());
        this.hud = new HUDPanel(scene);

        canvas.setFocusTraversalKeysEnabled(false);
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();

        this.input = new InputManager(new Keyboard(), new Mouse(), scene, hud);
        this.input.injectListeners(canvas);



        java.util.List<Attribute<?>> list = new ArrayList<>();
        list.add(new BasicAttribute<>("Position", new Point3D(0, 0, 3)));
        list.add(new BasicAttribute<>("Rotation", new Quaternion(1, 0, 0, 0)));
        list.add(new BasicAttribute<>("Scale", 2.0f));



        Cube c1 = new Cube(
                new CubeAttributes(5.0f),
                new BasicAttribute<>("Position", new Point3D(0, 5, 0)),
                new BasicAttribute<>("Scale", 2.0f)
        );

        Cube c2 = new Cube(
                new CubeAttributes(0.1f),
                new BasicAttribute<>("Position", new Point3D(3, 2, 1)),
                new BasicAttribute<>("Scale", 2.0f)
        );

        Cube c3 = new Cube(
                new CubeAttributes(2.0f),
                new BasicAttribute<>("Position", new Point3D(1, 8, 0)),
                new BasicAttribute<>("Scale", 1.0f),
                new BasicAttribute<>("Wireframe",true),
                new BasicAttribute<>("Backface Culling", false)
        );
        scene.addObject(c1);
        scene.addObject(c2);
        scene.addObject(c3);
        Sphere s1 = new Sphere();


        Sphere s2 = new Sphere(
                new SphereAttributes(21f, 20, 20),
                new BasicAttribute<>("Position", new Point3D(0, 1, 0)),
                new BasicAttribute<>("Wireframe",true),
                new BasicAttribute<>("Backface Culling", false),
                new BasicAttribute<>("Scale", 4.0f)
        );
        scene.addObject(s1);
        scene.addObject(s2);

        Tube t1 = new Tube(0,0,0,2,6,6);
        t1.setRotationSpeed(0.02f, 0.03f, 0);
        scene.addObject(t1);

        Grid worldGrid = new Grid(10, 20);
        scene.addObject(worldGrid);


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
                    canvas.getWidth(), canvas.getHeight());
        }


        Graphics2D g = colorBuffer.getImg().createGraphics();
        if (g != null) {
            hud.render(g);
            g.dispose();
        }

        input.tick();
        canvas.repaint();
    }

}
