package com.footballaigame.client.customdatatypes;

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
    public Vector() {
    }
    
    /**
     * Initializes a new instance of the {@link Vector} class.
     *
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
     * Returns the vector getLength.
     *
     * @return The vector getLength.
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
    
    /**
     * Returns the vector getLength squared.
     *
     * @return The getLength squared.
     */
    public double getLengthSquared() {
        return x * x + y * y;
    }
    
    /**
     * Normalizes this instance.
     */
    public void normalize() {
        double length = getLength();
        x /= length;
        y /= length;
    }
    
    /**
     * Returns the getNormalized vector of the current instance.
     *
     * @return The getNormalized vector of the current instance.
     */
    public Vector getNormalized() {
        Vector res = new Vector(x, y);
        res.normalize();
        return res;
    }
    
    public Vector getMultiplied(double scalar) {
        Vector result = new Vector(x, y);
        result.multiply(scalar);
        
        return result;
    }
    
    public Vector getResized(double newSize) {
        Vector res = getNormalized();
        res.multiply(newSize);
        return res;
    }
    
    public Vector getTruncated(double maxLength) {
        Vector res = new Vector(x, y);
        res.truncate(maxLength);
        return res;
    }
    
    /**
     * Resizes the current instance to the specified new getLength.
     *
     * @param newSize The new getLength.
     */
    public void resize(double newSize) {
        normalize();
        x *= newSize;
        y *= newSize;
    }
    
    public void truncate(double maxLength) {
        if (getLength() > maxLength)
            resize(maxLength);
    }
    
    public void multiply(double scalar) {
        x *= scalar;
        y *= scalar;
    }
    
    /**
     * Returns the distances between the given vectors.
     *
     * @param firstVector  The first vector.
     * @param secondVector The second vector.
     * @return The distance between the given vectors.
     */
    public static double getDistanceBetween(Vector firstVector, Vector secondVector) {
        return Math.sqrt(Math.pow(firstVector.x - secondVector.x, 2) + Math.pow(firstVector.y - secondVector.y, 2));
    }
    
    /**
     * Returns the squared distances between the given vectors.
     *
     * @param firstVector  The first vector.
     * @param secondVector The second vector.
     * @return The distance between the given vectors.
     */
    public static double getDistanceBetweenSquared(Vector firstVector, Vector secondVector) {
        return Math.pow(firstVector.x - secondVector.x, 2) + Math.pow(firstVector.y - secondVector.y, 2);
    }
    
    /**
     * Returns the dot product of the given vectors.
     *
     * @param firstVector  The first vector.
     * @param secondVector The second vector.
     * @return The dot product between the given vectors.
     */
    public static double getDotProduct(Vector firstVector, Vector secondVector) {
        return firstVector.x * secondVector.x + firstVector.y * secondVector.y;
    }
    
    public static Vector getDifference(Vector to, Vector from) {
        return new Vector(to.x - from.x, to.y - from.y);
    }
    
    /**
     * Returns the getSum of the given vectors.
     *
     * @param first  The first vector.
     * @param second The second vector.
     * @return The getSum of the given vectors.
     */
    public static Vector getSum(Vector first, Vector second) {
        return new Vector(first.x + second.x, first.y + second.y);
    }
}
