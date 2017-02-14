package FootballAIGame.SimulationEntities;

import FootballAIGame.CustomDataTypes.Vector;

public class Ball {
    
    public static final double ballDecelleration = 1.5; // [m/s/s]
    
    public Vector position;
    
    public Vector movement;
    
    public double currentSpeed;
    
    public Ball() {
        this.position = new Vector();
        this.movement = new Vector();
    }
    
}
