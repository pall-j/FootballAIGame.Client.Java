package FootballAIGame.SimulationEntities;

import FootballAIGame.CustomDataTypes.Vector;
import FootballAIGame.GameClient;

public class FootballPlayer {
    
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
     * Returns the player's current speed in meters per simulation step.
     * @return The player's current speed in meters per simulation step.
     */
    public double currentSpeed() {
        return movement.length();
    }
    
    /**
     * Returns the maximum allowed speed of the player in meters per simulation step.
     * @return The maximum allowed speed of the player in meters per simulation step.
     */
    public double maxSpeed() {
        return (5 + speed * 2.5 / 0.4) * GameClient.stepInterval / 1000;
    }
    
    /**
     * Returns the maximum allowed kick speed in meters per simulation step of football player.
     * @return The maximum allowed kick speed in meters per simulation step of football player.
     */
    public double maxKickSpeed() {
        return (15 + kickPower*10) * GameClient.stepInterval / 1000;
    }
    
    /**
     * Returns the maximum allowed acceleration in meters per simulation step squared of football player.
     * @return The maximum allowed acceleration in meters per simulation step squared of football player.
     */
    public double maxAcceleration() {
        return 3 * GameClient.stepInterval / 1000;
    }
    
}
