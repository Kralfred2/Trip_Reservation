package view;

import attributes.Attribute;
import attributes.AttributeGroup;
import objectdata.BaseObject;
import objectdata.Renderable;
import controller.Point3D;
import java.awt.*;

public class HUDPanel {
    private final Scene scene;
    public final Rectangle panelBounds = new Rectangle(10, 10, 220, 140);
    public final Rectangle wireframeBtn = new Rectangle(20, 90, 200, 20);
    public final Rectangle cullingBtn = new Rectangle(20, 110, 200, 20);

    public HUDPanel(Scene scene) {
        this.scene = scene;
    }

    public void render(Graphics2D g) {
        Renderable selected = scene.getPrimarySelected();
        if (!(selected instanceof BaseObject)) {
            drawDynamicPanel(g, 220, 60);
            g.setColor(Color.WHITE);
            g.drawString("No Object Selected", 20, 30);
            return;
        }

        BaseObject obj = (BaseObject) selected;
        FontMetrics fm = g.getFontMetrics(g.getFont());

        int spacing = 15;
        int maxWidth = 200;
        int totalLines = 1;

        // Pre-calculate width and height
        for (AttributeGroup group : obj.getFeatureGroups()) {
            for (Attribute<?> attr : group.getAttributes()) {
                String label = attr.getName() + ": " + formatValue(attr.getValue());
                int stringWidth = fm.stringWidth(label) + 40; // Add padding
                maxWidth = Math.max(maxWidth, stringWidth);
                totalLines++;
            }
        }

        int dynamicHeight = 40 + (totalLines * spacing) + (obj.getFeatureGroups().size() * 5);

        drawDynamicPanel(g, maxWidth, dynamicHeight);


        int x = 20;
        int y = 30;
        g.setColor(Color.YELLOW);
        g.drawString("Object: " + obj.getClass().getSimpleName(), x, y);
        y += spacing * 1.5;

        g.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

        for (AttributeGroup group : obj.getFeatureGroups()) {
            for (Attribute<?> attr : group.getAttributes()) {
                g.setColor(Color.WHITE);
                String label = attr.getName() + ": " + formatValue(attr.getValue());
                g.drawString(label, x + 10, y);
                y += spacing;
            }
            y += 5;
        }
    }

    private void drawDynamicPanel(Graphics2D g, int width, int height) {
        panelBounds.width = width;
        panelBounds.height = height;

        g.setColor(new Color(184, 159, 159, 180));
        g.fillRect(panelBounds.x, panelBounds.y, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(panelBounds.x, panelBounds.y, width, height);
    }

    private String formatValue(Object val) {
        if (val instanceof Point3D p) {
            return String.format("(%.1f, %.1f, %.1f)", p.x, p.y, p.z);
        } else if (val instanceof Float f) {
            return String.format("%.2f", f);
        }
        return val.toString();
    }
}
