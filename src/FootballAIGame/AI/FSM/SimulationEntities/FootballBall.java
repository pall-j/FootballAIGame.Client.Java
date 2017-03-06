package FootballAIGame.AI.FSM.SimulationEntities;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.GameClient;

public class FootballBall extends MovableEntity {
    
    public static final double MAX_DISTANCE_FOR_KICK = 1.5; // [m]
    
    /**
     * Gets the ball's deceleration in meters per simulation step squared.
     *
     * @return The ball's deceleration in meters per simulation step squared.
     */
    public static double ballDeceleration() {
        return 1.5 * Math.pow(GameClient.STEP_INTERVAL / 1000.0, 2);
    }
    
    public double timeToCoverDistance(double distance, double kickPower) {
        double v0 = kickPower;
        double a = ballDeceleration();
        double s = distance;
        
        // s = v0*t - 1/2 at^2   --> at^2 - 2v0t + 2s = 0  (quadratic equation)
        // smaller solution only - larger solution corresponds to the backwards movement
        double discriminant = 4 * v0 * v0 - 8 * s * a;
        if (discriminant < 0)
            return Double.POSITIVE_INFINITY; // ball will stop before target
        
        double t = (2 * v0 - Math.sqrt(discriminant)) / (2 * a);
        
        return t;
    }
    
    public Vector predictedPositionInTime(double time) {
        double finalSpeed = currentSpeed() - ballDeceleration() * time;
        
        if (Math.abs(currentSpeed()) < 0.001)
            return position;
        
        if (finalSpeed < 0)
            time = currentSpeed() / ballDeceleration(); // time to stop
        
        Vector diff = Vector.sum(movement.multiplied(time),
                movement.resized(-1 / 2.0 * ballDeceleration() * time * time));
        
        return Vector.sum(position, diff);
    }
}
