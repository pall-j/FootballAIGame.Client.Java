package FootballAIGame.SimulationEntities;

import FootballAIGame.CustomDataTypes.Vector;

public class Ball {
    
    /**
     * The ball's deceleration in meters per second squared.
     */
    public static final double ballDecelleration = 1.5; // [m/s/s]
    
    /**
     * The position of the ball.
     */
    public Vector position;
    
    /**
     * The movement vector of the ball. It describes how ball position changes in one simulation step.
     */
    public Vector movement;
    
    /**
     * The ball's current speed in meters per second.
     */
    public double currentSpeed;
    
    /**
     * Initializes a new instance of the {@link Ball} class.
     */
    public Ball() {
        this.position = new Vector();
        this.movement = new Vector();
    }
    
}
