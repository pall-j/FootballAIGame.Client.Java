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
    
    public Vector(Vector from, Vector to, double size) {
        x = to.x - from.x;
        y = to.y - from.y;
        resize(size);
    }
    
    public Vector(double x, double y, double size) {
        this.x = x;
        this.y = y;
        resize(size);
    }
    
    public Vector(Vector from, Vector to) {
        x = to.x - from.x;
        y = to.y - from.y;
    }
    
    /**
     * Returns the vector length.
     * @return The vector length.
     */
    public double length() {
        return Math.sqrt(x*x + y*y);
    }
    
    /**
     * Returns the vector length squared.
     * @return The length squared.
     */
    public double lengthSquared() { return x*x + y*y; }
    
    /**
     * Normalizes this instance.
     */
    public void normalize() {
        double length = length();
        x /= length;
        y /= length;
    }
    
    /**
     * Returns the normalized vector of the current instance.
     * @return The normalized vector of the current instance.
     */
    public Vector normalized() {
        Vector res = new Vector(x, y);
        res.normalize();
        return res;
    }
    
    public Vector multiplied(double scalar) {
        Vector result = new Vector(x, y);
        result.multiply(scalar);
        
        return result;
    }
    
    public Vector resized(double newSize) {
        Vector res = normalized();
        res.multiply(newSize);
        return res;
    }
    
    public Vector truncated(double maxLength) {
        Vector res = new Vector(x, y);
        res.truncate(maxLength);
        return res;
    }
    
    /**
     * Resizes the current instance to the specified new length.
     * @param newSize The new length.
     */
    public void resize(double newSize) {
        normalize();
        x *= newSize;
        y *= newSize;
    }
    
    public void truncate(double maxLength)  {
        if(length() > maxLength)
            resize(maxLength);
    }
    
    public void multiply(double scalar) {
        x *= scalar;
        y *= scalar;
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
     * Returns the squared distances between the given vectors.
     * @param firstVector The first vector.
     * @param secondVector The second vector.
     * @return The distance between the given vectors.
     */
    public static double distanceBetweenSquared(Vector firstVector, Vector secondVector) {
        return Math.pow(firstVector.x - secondVector.y, 2) + Math.pow(firstVector.y - secondVector.y, 2);
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
    
    public static Vector difference(Vector to, Vector from)
    {
        return new Vector(to.x - from.x, to.y - from.y);
    }
    
    /**
     * Returns the sum of the given vectors.
     * @param first The first vector.
     * @param second The second vector.
     * @return The sum of the given vectors.
     */
    public static Vector sum(Vector first, Vector second)
    {
        return new Vector(first.x + second.x, first.y + second.y);
    }
}
