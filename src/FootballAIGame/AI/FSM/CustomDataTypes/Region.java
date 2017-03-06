package FootballAIGame.AI.FSM.CustomDataTypes;

import FootballAIGame.AI.FSM.GameClient;

public class Region {
    
    public static final int NUMBER_OF_ROWS = 9;
    public static final int NUMBER_OF_COLUMNS = 8;
    
    public static int GetNumberOfRegions() {
        return NUMBER_OF_ROWS * NUMBER_OF_COLUMNS;
    }
    
    public static double GetRegionWidth() {
        return GameClient.FIELD_WIDTH / NUMBER_OF_COLUMNS;
    }
    
    public static double GetRegionHeight() {
        return GameClient.FIELD_HEIGHT / NUMBER_OF_ROWS;
    }
    
    public int id;
    
    public int x;
    
    public int y;
    
    public Vector center;
    
    public Vector topLeft;
    
    public Vector bottomRight;
    
    private Region(int id, int x, int y, Vector topLeft, Vector bottomRight) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        center = new Vector((topLeft.x + bottomRight.x) / 2, (topLeft.y + bottomRight.y) / 2);
    }
    
    private static Region[] regions;
    
    public static Region getRegion(int id) {
        if (regions == null)
            createRegions();
        
        if (id >= GetNumberOfRegions() || id < 0)
            throw new IndexOutOfBoundsException();
        
        return regions[id];
    }
    
    public static Region getRegion(int x, int y) {
        if (regions == null)
            createRegions();
        
        if (x >= NUMBER_OF_COLUMNS || x < 0 || y >= NUMBER_OF_ROWS || y < 0)
            throw new IndexOutOfBoundsException();
        
        return regions[x * NUMBER_OF_ROWS + y];
    }
    
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
