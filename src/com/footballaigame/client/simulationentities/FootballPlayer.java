package com.footballaigame.client.simulationentities;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;

public class FootballPlayer extends MovableEntity {
    
    public int id;
    
    /**
     * The speed parameter of the player. <p>
     * The max value should be 0.4.
     */
    public float speed;
    
    /**
     * The possession parameter of the player.
     * The max value should be 0.4.
     */
    public float possession;
    
    /**
     * The precision parameter of the player.
     * The max value should be 0.4.
     */
    public float precision;
    
    /**
     * The kickVector power parameter of the player.
     * The max value should be 0.4.
     */
    public float kickPower;
    
    /**
     * The kickVector of the player. <p>
     * It describes movement vector that ball would get if the kickVector was done with 100% precision.
     */
    public Vector kickVector;
    
    /**
     * Initializes a new instance of the {@link FootballPlayer} class.
     */
    public FootballPlayer(int id) {
        kickVector = new Vector();
        this.id = id;
    }
    
    /**
     * Returns the player's current speed in meters per simulation step.
     *
     * @return The player's current speed in meters per simulation step.
     */
    public double currentSpeed() {
        return movement.length();
    }
    
    /**
     * Returns the maximum allowed speed of the player in meters per simulation step.
     *
     * @return The maximum allowed speed of the player in meters per simulation step.
     */
    public double maxSpeed() {
        return (4 + speed * 2 / 0.4) * GameClient.STEP_INTERVAL / 1000.0;
    }
    
    /**
     * Returns the maximum allowed kickVector speed in meters per simulation step of football player.
     *
     * @return The maximum allowed kickVector speed in meters per simulation step of football player.
     */
    public double maxKickSpeed() {
        return (15 + kickPower * 5) * GameClient.STEP_INTERVAL / 1000.0;
    }
    
    /**
     * Returns the maximum allowed acceleration in meters per simulation step squared of football player.
     *
     * @return The maximum allowed acceleration in meters per simulation step squared of football player.
     */
    public double maxAcceleration() {
        return 5 * Math.pow(GameClient.STEP_INTERVAL / 1000.0, 2);
    }
    
    public boolean canKickBall(FootballBall ball) {
        return Vector.distanceBetween(position, ball.position) <= FootballBall.MAX_DISTANCE_FOR_KICK;
    }
    
    public void kickBall(FootballBall ball, Vector target) {
        kickBall(ball, target, maxKickSpeed());
    }
    
    public void kickBall(FootballBall ball, Vector target, double kickAcceleration) {
        if (kickAcceleration > maxKickSpeed())
            kickAcceleration = maxKickSpeed();
        kickVector = new Vector(ball.position, target, kickAcceleration);
    }
    
    public Vector passBall(FootballBall ball, FootballPlayer passTarget) {
        double time = ball.timeToCoverDistance(Vector.distanceBetween(ball.position, passTarget.position), maxKickSpeed());
        Vector nextPos = passTarget.predictedPositionInTime(time);
        kickBall(ball, nextPos);
        return nextPos;
    }
    
    public static double DotProduct(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }
    
    public double timeToGetToTarget(Vector target) {
        // this is only approx. (continuous acceleration)
        
        Vector toTarget = Vector.difference(target, position);
        if (toTarget.length() < 0.001)
            return 0;
        
        double v0 = Vector.dotProduct(toTarget, movement) / toTarget.length();
        double v1 = maxSpeed();
        double a = maxAcceleration();
        double t1 = (v1 - v0) / a;
        double s = Vector.distanceBetween(position, target);
        
        double s1 = v0 * t1 + 1 / 2.0 * a * t1 * t1; // distance traveled during acceleration
        if (s1 >= s) // target reached during acceleration
        {
            double discriminant = 4 * v0 * v0 + 8 * a * s;
            return (-2 * v0 + Math.sqrt(discriminant)) / (2 * a);
        }
        
        double s2 = s - s1; // distance traveled during max speed
        double t2 = s2 / v1;
        
        return t1 + t2;
    }
}
