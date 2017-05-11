package com.footballaigame.client.customdatatypes;

import com.footballaigame.client.GameClient;

/**
 * Represents the region of the football field.
 */
public class Region {
    
    /**
     * The number of rows into which the field is divided.
     */
    public static final int NUMBER_OF_ROWS = 9;
    
    /**
     * The number of columns into which the field is divided.
     */
    public static final int NUMBER_OF_COLUMNS = 8;
    
    /**
     * The identifier.
     */
    public int id;
    
    /**
     * The x coordinate in the regions' grid.
     */
    public int x;
    
    /**
     * The y coordinate in the regions' grid.
     */
    public int y;
    
    /**
     * The center position of the region.
     */
    public Vector center;
    
    /**
     * The top left position of the region.
     */
    public Vector topLeft;
    
    /**
     * The bottom right position of the region.
     */
    public Vector bottomRight;
    
    /**
     * The array that should contain all regions.
     */
    private static Region[] regions;
    
    /**
     * Initializes a new instance of the {@link Region} class.
     * @param id The identifier.
     * @param x The x coordinate in the regions' grid.
     * @param y The y coordinate in the regions' grid.
     * @param topLeft The top left position of the region.
     * @param bottomRight The bottom right position of the region.
     */
    private Region(int id, int x, int y, Vector topLeft, Vector bottomRight) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        center = new Vector((topLeft.x + bottomRight.x) / 2, (topLeft.y + bottomRight.y) / 2);
    }
    
    /**
     * Gets the number of regions.
     * @return The number of field regions.
     */
    public static int GetNumberOfRegions() {
        return NUMBER_OF_ROWS * NUMBER_OF_COLUMNS;
    }
    
    /**
     * Gets the width of one region.
     * @return The width of one region.
     */
    public static double GetRegionWidth() {
        return GameClient.FIELD_WIDTH / NUMBER_OF_COLUMNS;
    }
    
    /**
     * Gets the height of one region.
     * @return The height of one region.
     */
    public static double GetRegionHeight() {
        return GameClient.FIELD_HEIGHT / NUMBER_OF_ROWS;
    }
    
    /**
     * Gets the region with the specified identifier.
     * @param id The region's identifier.
     * @return The corresponding {@link Region}.
     * @throws IndexOutOfBoundsException Thrown if an invalid identifier was specified.
     */
    public static Region getRegion(int id) throws IndexOutOfBoundsException {
        if (regions == null)
            createRegions();
        
        if (id >= GetNumberOfRegions() || id < 0)
            throw new IndexOutOfBoundsException();
        
        return regions[id];
    }
    
    /**
     * Gets the region with the specified grid coordinates.
     * @param x The x coordinate in the regions' grid.
     * @param y The y coordinate in the regions' grid.
     * @return The corresponding {@link Region}.
     * @throws IndexOutOfBoundsException Thrown if invalid coordinates were specified.
     */
    public static Region getRegion(int x, int y) throws IndexOutOfBoundsException {
        if (regions == null)
            createRegions();
        
        if (x >= NUMBER_OF_COLUMNS || x < 0 || y >= NUMBER_OF_ROWS || y < 0)
            throw new IndexOutOfBoundsException();
        
        return regions[x * NUMBER_OF_ROWS + y];
    }
    
    /**
     * Creates the regions in accordance with the {@link Region#NUMBER_OF_ROWS} and {@link Region#NUMBER_OF_COLUMNS}
     * and adds them into {@link Region#regions}.
     */
    private static void createRegions() {
        regions = new Region[GetNumberOfRegions()];
        for (int x = 0; x < NUMBER_OF_COLUMNS; x++) {
            for (int y = 0; y < NUMBER_OF_ROWS; y++) {
                regions[x * NUMBER_OF_ROWS + y] = new Region(x * NUMBER_OF_ROWS + y, x, y,
                        new Vector(x * GetRegionWidth(), y * GetRegionHeight()),
                        new Vector((x + 1) * GetRegionWidth(), (y + 1) * GetRegionHeight()));
            }
        }
    }
    
}
