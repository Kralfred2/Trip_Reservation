package view;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private final Canvas panel;

    public Window(int width, int height) {
        panel = new Canvas(width, height);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("PGRF2 2026: Task 1 by Alfred Withers");

        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);

        setLocationRelativeTo(null);


        panel.setFocusable(true);
        panel.grabFocus();
    }

    public Canvas getCanvas() {
        return panel;
    }

}
