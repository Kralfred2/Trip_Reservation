package inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class Keyboard extends KeyAdapter {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<Integer> prevPressedKeys = new HashSet<>();
    private final Map<Integer, Action> keyMap = new HashMap<>();

    public Keyboard() {
        // Map physical keys to logical actions
        keyMap.put(KeyEvent.VK_W, Action.MOVE_FORWARD);
        keyMap.put(KeyEvent.VK_S, Action.MOVE_BACKWARD);
        keyMap.put(KeyEvent.VK_A, Action.MOVE_LEFT);
        keyMap.put(KeyEvent.VK_D, Action.MOVE_RIGHT);
        keyMap.put(KeyEvent.VK_TAB, Action.NEXT_SELECTION);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
         //System.out.println("Key Pressed Code: " + e.getKeyCode());
    }
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode) && !prevPressedKeys.contains(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) { pressedKeys.remove(e.getKeyCode()); }

    public void tick() {
        prevPressedKeys.clear();
        prevPressedKeys.addAll(pressedKeys);
    }

    public boolean isActionDown(Action action) {
        return keyMap.entrySet().stream()
                .filter(entry -> entry.getValue() == action)
                .anyMatch(entry -> pressedKeys.contains(entry.getKey()));
    }

    public boolean isActionPressed(Action action) {
        return keyMap.entrySet().stream()
                .filter(entry -> entry.getValue() == action)
                .anyMatch(entry -> pressedKeys.contains(entry.getKey()) && !prevPressedKeys.contains(entry.getKey()));
    }
}