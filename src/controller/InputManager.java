package controller;

import inputs.Keyboard;
import inputs.Mouse;
import objectdata.BaseObject;
import objectdata.Renderable;
import view.HUDPanel;
import view.Scene;
import view.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static inputs.Action.*;

public class InputManager {
    private final Keyboard keyboard;
    private final Mouse mouse;
    private final Scene scene;
    private int currentSelectionIndex = 0;
    private float moveSpeed = 0.1f;
    private float sensitivity = 0.01f;
    private final HUDPanel hud;

    public InputManager(Keyboard k, Mouse m, Scene s, HUDPanel h) {
        this.keyboard = k;
        this.mouse = m;
        this.scene = s;
        this.hud = h;
    }
    public void injectListeners(JPanel panel){
        panel.addKeyListener(keyboard);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
    }

    public void processInput(float deltaTime) {
        Camera cam = scene.getCamera();


        float sinY = (float) Math.sin(cam.rotY);
        float cosY = (float) Math.cos(cam.rotY);

        if (keyboard.isActionDown(MOVE_FORWARD)) {
            cam.x += sinY * moveSpeed;
            cam.z += cosY * moveSpeed;
        }
        if (keyboard.isActionDown(MOVE_BACKWARD)) {
            cam.x -= sinY * moveSpeed;
            cam.z -= cosY * moveSpeed;
        }
        if (keyboard.isActionDown(MOVE_LEFT)){
            cam.x -= cosY * moveSpeed;
            cam.z += sinY * moveSpeed;
        }
        if (keyboard.isActionDown(MOVE_RIGHT)){
            cam.x += cosY * moveSpeed;
            cam.z -= sinY * moveSpeed;
        }
        if (keyboard.isKeyPressed(KeyEvent.VK_TAB)) {
            cycleSelection();
        }

        if (mouse.isRightClickHeld()) {
            cam.rotY += mouse.getPullDeltaX() * sensitivity;
            cam.rotX -= mouse.getPullDeltaY() * sensitivity;

            cam.rotX = Math.max(-1.5f, Math.min(1.5f, cam.rotX));
        }

        float sinX = (float) Math.sin(cam.rotX);
        float cosX = (float) Math.cos(cam.rotX);

        if (keyboard.isActionDown(MOVE_FORWARD)) {
            cam.x += sinY * cosX * moveSpeed;
            cam.y -= sinX * moveSpeed;
            cam.z += cosY * cosX * moveSpeed;
        }
        if (keyboard.isActionDown(MOVE_BACKWARD)) {
            cam.x -= sinY * cosX * moveSpeed;
            cam.y += sinX * moveSpeed;
            cam.z -= cosY * cosX * moveSpeed;
        }




    }
    private boolean handleHudClick(Point p) {
        Renderable selected = scene.getPrimarySelected();
        if (!(selected instanceof BaseObject obj)) return false;


        return hud.panelBounds.contains(p);
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