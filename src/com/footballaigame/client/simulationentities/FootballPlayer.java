package com.footballaigame.client.simulationentities;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;

/**
 * Represents the football player in the simulation.
 */
public class FootballPlayer extends MovableEntity {
    
    /**
     * The identifier.
     */
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
     * It describes movement {@link Vector} that ball would get if the kick was done with 100% precision.
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
    public double getCurrentSpeed() {
        return movement.getLength();
    }
    
    /**
     * Returns the maximum allowed speed of the player in meters per simulation step.
     *
     * @return The maximum allowed speed of the player in meters per simulation step.
     */
    public double getMaxSpeed() {
        return (4 + speed * 2 / 0.4) * GameClient.STEP_INTERVAL / 1000.0;
    }
    
    /**
     * Returns the maximum allowed kickVector speed in meters per simulation step of football player.
     *
     * @return The maximum allowed kickVector speed in meters per simulation step of football player.
     */
    public double getMaxKickSpeed() {
        return (15 + kickPower * 5) * GameClient.STEP_INTERVAL / 1000.0;
    }
    
    /**
     * Returns the maximum allowed acceleration in meters per simulation step squared of football player.
     *
     * @return The maximum allowed acceleration in meters per simulation step squared of football player.
     */
    public double getMaxAcceleration() {
        return 5 * Math.pow(GameClient.STEP_INTERVAL / 1000.0, 2);
    }
    
    /**
     * Determines whether the player can kick the specified ball.
     * @param ball The ball.
     * @return True if the player can kick the specified ball; otherwise, false.
     */
    public boolean canKickBall(FootballBall ball) {
        return Vector.getDistanceBetween(position, ball.position) <= FootballBall.MAX_DISTANCE_FOR_KICK;
    }
    
    /**
     * Kicks the ball to the specified target with the maximum kick speed. Sets the kick vector accordingly.
     * @param ball The ball.
     * @param target The kick target.
     */
    public void kickBall(FootballBall ball, Vector target) {
        kickBall(ball, target, getMaxKickSpeed());
    }
    
    /**
     * Kicks the ball to the specified target with the specified kick speed. Sets the kick vector accordingly.
     * @param ball The ball.
     * @param target The kick target.
     * @param kickSpeed The kick speed.
     */
    public void kickBall(FootballBall ball, Vector target, double kickSpeed) {
        if (kickSpeed > getMaxKickSpeed())
            kickSpeed = getMaxKickSpeed();
        kickVector = new Vector(ball.position, target, kickSpeed);
    }
    
    /**
     * Passes the specified ball to the specified player. Sets the kick vector accordingly.
     * @param ball The ball.
     * @param passTarget The pass target player.
     * @return The kick target.
     */
    public Vector passBall(FootballBall ball, FootballPlayer passTarget) {
        double time = ball.getTimeToCoverDistance(Vector.getDistanceBetween(ball.position, passTarget.position), getMaxKickSpeed());
        Vector nextPos = passTarget.predictPositionInTime(time);
        kickBall(ball, nextPos);
        return nextPos;
    }
    
    /**
     * Gets the approximated time to get to the specified position.
     * @param target The target position.
     * @return The approximated time to get to target.
     */
    public double getTimeToGetToTarget(Vector target) {
        // this is only approx. (continuous acceleration)
        
        Vector toTarget = Vector.getDifference(target, position);
        if (toTarget.getLength() < 0.001)
            return 0;
        
        double v0 = Vector.getDotProduct(toTarget, movement) / toTarget.getLength();
        double v1 = getMaxSpeed();
        double a = getMaxAcceleration();
        double t1 = (v1 - v0) / a;
        double s = Vector.getDistanceBetween(position, target);
        
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
