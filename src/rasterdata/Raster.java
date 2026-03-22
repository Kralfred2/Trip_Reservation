package rasterdata;

import java.awt.*;

/**
 * @author Kirsten
 * @version 2026.1
 */
public interface Raster {

    /**
     * Returns the number of columns in this Raster
     *
     * @return number of column
     */
    int getWidth();

    /**
     * Returns the number of rows in this Raster
     *
     * @return number of rows
     */
    int getHeight();

    /**
     * Retrieves color stored at the provided pixel address
     *
     * @param x column index
     * @param y row index
     * @return The color if pixel address is valid
     */
    int getPixel(int x, int y);

    /**
     * Updates the pixel at the provided pixel address
     *
     * @param x column index
     * @param y row index
     * @param pixelValue a color to write
     */
    void setPixel(int x, int y, Color pixelValue);

    void clear();

    default boolean isValidAddress(int x, int y) {
        // TODO check validity of the given address
        return false;
    }
}
