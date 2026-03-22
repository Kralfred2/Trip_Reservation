package inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    private int lastX, lastY;
    private int deltaX, deltaY;
    private boolean rightClickHeld = false;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) { // Right Click
            rightClickHeld = true;
            lastX = e.getX();
            lastY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) rightClickHeld = false;
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