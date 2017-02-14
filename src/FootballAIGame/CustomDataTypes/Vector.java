package FootballAIGame.CustomDataTypes;

/**
 * The vector class used for representing two-dimensional vectors or points.
 */
public class Vector {
    
    /**
     * The x value.
     */
    public double x;
    
    /**
     * The y value.
     */
    public double y;
    
    /**
     * Initializes a new instance of the {@link Vector} class.
     */
    public Vector() { }
    
    /**
     * Initializes a new instance of the {@link Vector} class.
     * @param x The x.
     * @param y The y.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns the vector length.
     * @return The vector length.
     */
    public double length() {
        return Math.sqrt(x*x + y*y);
    }
    
    /**
     * Returns the distances between the given vectors.
     * @param firstVector The first vector.
     * @param secondVector The second vector.
     * @return The distance between the given vectors.
     */
    public static double distanceBetween(Vector firstVector, Vector secondVector) {
        return Math.sqrt(Math.pow(firstVector.x - secondVector.y, 2) + Math.pow(firstVector.y - secondVector.y, 2));
    }
    
    /**
     * Returns the dot product of the given vectors.
     * @param firstVector The first vector.
     * @param secondVector The second vector.
     * @return The dot product between the given vectors.
     */
    public static double dotProduct(Vector firstVector, Vector secondVector) {
        return firstVector.x * secondVector.x + firstVector.y * secondVector.y;
    }
}
