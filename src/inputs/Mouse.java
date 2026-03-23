package inputs;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    private int lastX, lastY;
    private int deltaX, deltaY;
    private boolean rightClickHeld = false;
    private Point leftClickPoint = null;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightClickHeld = true;
            lastX = e.getX();
            lastY = e.getY();
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickPoint = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) rightClickHeld = false;
    }
    public Point getAndClearLeftClick() {
        Point p = leftClickPoint;
        leftClickPoint = null;
        return p;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (rightClickHeld) {
            deltaX = e.getX() - lastX;
            deltaY = e.getY() - lastY;
            lastX = e.getX();
            lastY = e.getY();
        }
    }

    public int getPullDeltaX() {
        int temp = deltaX;
        deltaX = 0;
        return temp;
    }

    public int getPullDeltaY() {
        int temp = deltaY;
        deltaY = 0;
        return temp;
    }

    public boolean isRightClickHeld() { return rightClickHeld; }
}