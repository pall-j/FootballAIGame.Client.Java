package FootballAIGame.SimulationEntities;

import FootballAIGame.CustomDataTypes.Vector;
import FootballAIGame.GameClient;

public class FootballPlayer {
    
    public static final int maxAcceleration = 3; // [m/s/s]
    
    public float speed;
    
    public float possession;
    
    public float precision;
    
    public float kickPower;
    
    public Vector position;
    
    public Vector movement;
    
    public Vector kick;
    
    public FootballPlayer() {
        position = new Vector();
        movement = new Vector();
        kick = new Vector();
    }
    
    public double currentSpeed() {
        return movement.length()*1000 / GameClient.stepInterval;
    }
    
    public double maxSpeed() {
        return 5 + speed * 2.5 / 0.4;
    }
    
}
