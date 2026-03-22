package view;

import objectdata.Renderable;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Renderable> objects = new ArrayList<>();
    private final Camera camera;
    private final List<Renderable> selectedObjects = new ArrayList<>();

    public Scene() {
        this.camera = new Camera(0, 0, 0);
    }

    public void addObject(Renderable obj) {
        objects.add(obj);
    }

    public List<Renderable> getObjects() {
        return objects;
    }
    public void selectObject(Renderable obj, boolean multiSelect) {
        if (!multiSelect) {
            // Deselect everything else first
            for (Renderable r : selectedObjects) r.setSelected(false);
            selectedObjects.clear();
        }

        obj.setSelected(true);
        if (!selectedObjects.contains(obj)) {
            selectedObjects.add(obj);
        }
    }

    public List<Renderable> getSelectedObjects() {
        return selectedObjects;
    }

    public void clearSelection() {
        for (Renderable r : selectedObjects) r.setSelected(false);
        selectedObjects.clear();
    }

    public Camera getCamera() {
        return camera;
    }

    public void update(float deltaTime) {
        for (Renderable obj : objects) {
            obj.update(deltaTime);
        }
    }
}
