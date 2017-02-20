package FootballAIGame.SimulationEntities;

import FootballAIGame.CustomDataTypes.Vector;
import FootballAIGame.GameClient;

public class Ball {
    
    /**
     * The position of the ball.
     */
    public Vector position;
    
    /**
     * The movement vector of the ball. It describes how ball position changes in one simulation step.
     */
    public Vector movement;
    
    /**
     * The ball's current speed in meters per simulation step.
     */
    public double currentSpeed;
    
    /**
     * Initializes a new instance of the {@link Ball} class.
     */
    public Ball() {
        this.position = new Vector();
        this.movement = new Vector();
    }
    
    /**
     * Gets the ball's deceleration in meters per simulation step squared.
     * @return The ball's deceleration in meters per simulation step squared.
     */
    public static double ballDecelleration() {
        return 1.5 * GameClient.stepInterval / 1000;
    }
    
}
