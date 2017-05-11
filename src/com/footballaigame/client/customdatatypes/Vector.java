package com.footballaigame.client.customdatatypes;

/**
 * Represents the two-dimensional vector or point.
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
    public Vector() {
    }
    
    /**
     * Initializes a new instance of the {@link Vector} class.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initializes a new instance of the {@link Vector} class.
     * The vector is specified by the parameters and is equal to (to - target).
     * @param from From which position.
     * @param to To which position.
     */
    public Vector(Vector from, Vector to) {
        x = to.x - from.x;
        y = to.y - from.y;
    }
    
    /**
     * Initializes a new instance of the {@link Vector} class.
     * The vector is specified by the parameters and is equal to (to - target).
     * @param from From which position.
     * @param to To which position.
     * @param length The length of the vector.
     */
    public Vector(Vector from, Vector to, double length) {
        x = to.x - from.x;
        y = to.y - from.y;
        resize(length);
    }
    
    /**
     * Initializes a new instance of the {@link Vector} class.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param length The length of the vector.
     */
    public Vector(double x, double y, double length) {
        this.x = x;
        this.y = y;
        resize(length);
    }
    
    /**
     * Gets the vector's length.
     * @return The vector' length.
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
    
    /**
     * Gets the vector's squared length.
     * @return The squared length of the vector.
     */
    public double getLengthSquared() {
        return x * x + y * y;
    }
    
    /**
     * Gets the normalized vector of the current vector.
     * @return The normalized vector of the current vector.
     */
    public Vector getNormalized() {
        Vector res = new Vector(x, y);
        res.normalize();
        return res;
    }
    
    /**
     * Gets the multiplied vector of the current vector.
     * @param scalar The scalar value for multiplication.
     * @return The multiplied vector of the current vector.
     */
    public Vector getMultiplied(double scalar) {
        Vector result = new Vector(x, y);
        result.multiply(scalar);
        
        return result;
    }
    
    /**
     * Gets the resized vector of the current vector.
     * @param newSize The length of the resized vector.
     * @return The resized vector of the current vector.
     */
    public Vector getResized(double newSize) {
        Vector res = getNormalized();
        res.multiply(newSize);
        return res;
    }
    
    /**
     * Gets the truncated vector to the specified maximum length if the vector had longer length.
     * @param maxLength The maximum length.
     * @return The truncated vector to the specified length if the vector had longer length.
     */
    public Vector getTruncated(double maxLength) {
        Vector res = new Vector(x, y);
        res.truncate(maxLength);
        return res;
    }
    
    /**
     * Normalizes the vector.
     */
    public void normalize() {
        double length = getLength();
        x /= length;
        y /= length;
    }
    
    
    /**
     * Resizes the vector to the specified new length.
     * @param newLength The new length.
     */
    public void resize(double newLength) {
        normalize();
        x *= newLength;
        y *= newLength;
    }
    
    /**
     * Truncates the vector to the specified maximum length if the vector had longer length.
     * @param maxLength The maximum length.
     */
    public void truncate(double maxLength) {
        if (getLength() > maxLength)
            resize(maxLength);
    }
    
    /**
     * Multiplies the vector by the specified scalar value.
     * @param scalar The scalar.
     */
    public void multiply(double scalar) {
        x *= scalar;
        y *= scalar;
    }
    
    /**
     * Gets the distances between the given vectors.
     * @param firstVector  The first vector.
     * @param secondVector The second vector.
     * @return The distance between the given vectors.
     */
    public static double getDistanceBetween(Vector firstVector, Vector secondVector) {
        return Math.sqrt(Math.pow(firstVector.x - secondVector.x, 2) + Math.pow(firstVector.y - secondVector.y, 2));
    }
    
    /**
     * Returns the squared distances between the given vectors.
     * @param firstVector  The first vector.
     * @param secondVector The second vector.
     * @return The squared distance between the given vectors.
     */
    public static double getDistanceBetweenSquared(Vector firstVector, Vector secondVector) {
        return Math.pow(firstVector.x - secondVector.x, 2) + Math.pow(firstVector.y - secondVector.y, 2);
    }
    
    /**
     * Gets the dot product of the given vectors.
     * @param firstVector  The first vector.
     * @param secondVector The second vector.
     * @return The dot product between the given vectors.
     */
    public static double getDotProduct(Vector firstVector, Vector secondVector) {
        return firstVector.x * secondVector.x + firstVector.y * secondVector.y;
    }
    
    /**
     * Gets the difference between the specified vectors.
     * The difference {@link Vector} is equal to (to - target).
     * @param to To.
     * @param from From.
     * @return The difference {@link Vector} that is equal to (to - target).
     */
    public static Vector getDifference(Vector to, Vector from) {
        return new Vector(to.x - from.x, to.y - from.y);
    }
    
    /**
     * Returns the sum of the given vectors.
     * @param first  The first vector.
     * @param second The second vector.
     * @return The sum {@link Vector} of the given vectors.
     */
    public static Vector getSum(Vector first, Vector second) {
        return new Vector(first.x + second.x, first.y + second.y);
    }
}
