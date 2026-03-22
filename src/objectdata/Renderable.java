package objectdata;

import rasterdata.ColorBuffer;
import rasterdata.DepthBuffer;
import view.Camera;

public interface Renderable {
    /**
     * Updates the object's logic (animation, physics, etc.)
     * @param deltaTime The time elapsed since the last frame.
     */
    void update(float deltaTime);
    void setRotationSpeed(float vx, float vy, float vz);
    void setSelected(boolean selected);
    public boolean isSelected();
    /**
     * Draws the object to the buffers.
     */
    void render(Camera camera, ColorBuffer cb, DepthBuffer db, int screenW, int screenH, boolean filled, boolean wireframe, boolean backFaceCulling);
}