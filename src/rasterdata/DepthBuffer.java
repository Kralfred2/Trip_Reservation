package rasterdata;

public class DepthBuffer implements Raster {
    private final float[] data;
    private final int width, height;

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new float[width * height];
        clear();
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = Float.POSITIVE_INFINITY;
        }
    }

    public float getDepth(int x, int y) {
        if (isValidAddress(x, y)) return data[y * width + x];
        return Float.POSITIVE_INFINITY;
    }

    public void setDepth(int x, int y, float z) {
        if (isValidAddress(x, y)) data[y * width + x] = z;
    }

    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
    @Override public boolean isValidAddress(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override public int getPixel(int x, int y) { return 0; }
    @Override public void setPixel(int x, int y, java.awt.Color v) {}
}
