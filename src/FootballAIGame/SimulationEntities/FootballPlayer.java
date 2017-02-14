package FootballAIGame.SimulationEntities;

import FootballAIGame.CustomDataTypes.Vector;
import FootballAIGame.GameClient;

public class FootballPlayer {
    
    /**
     * The maximum allowed acceleration in meters per second squared of football player.
     */
    public static final int maxAcceleration = 3; // [m/s/s]
    
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
     * The kick power parameter of the player.
     * The max value should be 0.4.
     */
    public float kickPower;
    
    /**
     * The position of the player.
     */
    public Vector position;
    
    /**
     * The movement vector of the player.
     */
    public Vector movement;
    
    /**
     * The kick vector of the player. <p>
     * It describes movement vector that ball would get if the kick was done with 100% precision.
     */
    public Vector kick;
    
    /**
     * Initializes a new instance of the {@link FootballPlayer} class.
     */
    public FootballPlayer() {
        position = new Vector();
        movement = new Vector();
        kick = new Vector();
    }
    
    /**
     * Returns the player's current speed in meters per second.
     * @return The player's current speed in meters per second.
     */
    public double currentSpeed() {
        return movement.length()*1000 / GameClient.stepInterval;
    }
    
    /**
     * Returns the maximum allowed speed of the player in meters per second.
     * @return The maximum allowed speed of the player in meters per second.
     */
    public double maxSpeed() {
        return 5 + speed * 2.5 / 0.4;
    }
    
}
