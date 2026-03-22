package controller;

import inputs.Keyboard;
import inputs.Mouse;
import objectdata.Renderable;
import view.Scene;
import view.Camera;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static inputs.Action.*;

public class InputManager {
    private final Keyboard keyboard;
    private final Mouse mouse;
    private final Scene scene;
    private int currentSelectionIndex = 0;
    private float moveSpeed = 0.1f;
    private float sensitivity = 0.01f;


    public InputManager(Keyboard k, Mouse m, Scene s) {
        this.keyboard = k;
        this.mouse = m;
        this.scene = s;
    }
    public void injectListeners(JPanel panel){
        panel.addKeyListener(keyboard);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
    }

    public void processInput(float deltaTime) {
        Camera cam = scene.getCamera();

        if (keyboard.isActionDown(MOVE_FORWARD)) cam.z += moveSpeed;
        if (keyboard.isActionDown(MOVE_BACKWARD)) cam.z -= moveSpeed;
        if (keyboard.isActionDown(MOVE_LEFT)) cam.x -= moveSpeed;
        if (keyboard.isActionDown(MOVE_RIGHT)) cam.x += moveSpeed;
        if (keyboard.isKeyPressed(KeyEvent.VK_TAB)) {
            cycleSelection();
        }



        if (mouse.isRightClickHeld()) {
            cam.rotY += mouse.getPullDeltaX() * sensitivity;
            cam.rotX += mouse.getPullDeltaY() * sensitivity;
        }


    }
    public void tick() {
        keyboard.tick();
    }
    private void cycleSelection() {
        if (scene.getObjects().isEmpty()) return;

        currentSelectionIndex = (currentSelectionIndex + 1) % scene.getObjects().size();
        Renderable target = scene.getObjects().get(currentSelectionIndex);
        scene.selectObject(target, false);

        System.out.println("Selected: " + target.getClass().getSimpleName());
    }

}