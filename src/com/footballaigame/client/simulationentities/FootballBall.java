package com.footballaigame.client.simulationentities;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;

/**
 * Represents the football ball in the simulation.
 */
public class FootballBall extends MovableEntity {
    
    /**
     * The maximum allowed distance for kick to be successful.
     */
    public static final double MAX_DISTANCE_FOR_KICK = 2;
    
    /**
     * Returns the ball's deceleration in meters per simulation step squared.
     *
     * @return The ball's deceleration in meters per simulation step squared.
     */
    public static double getBallDeceleration() {
        return 1.5 * Math.pow(GameClient.STEP_INTERVAL / 1000.0, 2);
    }
    
    /**
     * Gets the time that is needed to cover the specified distance.
     * @param distance The distance.
     * @param kickPower The kick power that would be applied.
     * @return The time that is need to cover the specified distance.
     */
    public double getTimeToCoverDistance(double distance, double kickPower) {
       
        double v0 = kickPower;
        double a = getBallDeceleration();
        double s = distance;
        
        // s = v0*t - 1/2 at^2   --> at^2 - 2v0t + 2s = 0  (quadratic equation)
        // smaller solution only - larger solution corresponds to the backwards movement
        double discriminant = 4 * v0 * v0 - 8 * s * a;
        if (discriminant < 0)
            return Double.POSITIVE_INFINITY; // ball will stop before target
        
        double t = (2 * v0 - Math.sqrt(discriminant)) / (2 * a);
        
        return t;
    }
    
    /**
     * Predicts the position in time.
     * @param time The time.
     * @return The predicted position {@link Vector}.
     */
    public Vector predictPositionInTime(double time) {
    
        return predictPositionInTimeAfterKick(time, movement);
    }
    
    /**
     * Predicts the position in time after kick.
     * @param time The time.
     * @param kick The kick.
     * @return The predicted position {@link Vector}.
     */
    public Vector predictPositionInTimeAfterKick(double time, Vector kick) {
        double kickSpeed = kick.getLength();
        
        double finalSpeed = kickSpeed - getBallDeceleration() * time;
        
        if (Math.abs(kickSpeed) < 0.001)
            return position;
        
        if (finalSpeed < 0 || Double.isInfinite(time))
            time = kickSpeed / getBallDeceleration(); // time to stop
        
        Vector diff = Vector.getSum(kick.getResized(time * kickSpeed),
                kick.getResized(-1 / 2.0 * getBallDeceleration() * time * time));
        
        return Vector.getSum(position, diff);
    }
}
