package rasterdata;

import transforms.Point2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorBuffer implements Raster {

    private final BufferedImage img;
    private final Color backgroundColor = new Color(0x8D061A);

    public ColorBuffer(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        if (isValidAddress(x, y)) {
            return img.getRGB(x, y);
        }
        return 0;
    }
    @Override
    public boolean isValidAddress(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }


    public BufferedImage drawLine(){
        img.createGraphics();


        return img;
    }

    @Override
    public void setPixel(int x, int y, Color pixelValue) {
        img.setRGB(x,y,pixelValue.getRGB());
    }

    @Override
    public void clear() {
        Graphics g = img.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);
    }

    public void present(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    public BufferedImage getImg() {
        return img;
    }

}
