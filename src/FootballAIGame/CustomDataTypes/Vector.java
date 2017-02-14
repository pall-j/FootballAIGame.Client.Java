package FootballAIGame.CustomDataTypes;

public class Vector {
    
    public double x;
    
    public double y;
    
    public Vector() { }
    
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double length() {
        return Math.sqrt(x*x + y*y);
    }
    
    public static double distanceBetween(Vector firstVector, Vector secondVector) {
        return Math.sqrt(Math.pow(firstVector.x - secondVector.y, 2) + Math.pow(firstVector.y - secondVector.y, 2));
    }
    
    public static double dotProduct(Vector firstVector, Vector secondVector) {
        return firstVector.x * secondVector.x + firstVector.y * secondVector.y;
    }
}
