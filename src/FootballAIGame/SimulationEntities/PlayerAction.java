package FootballAIGame.SimulationEntities;

import FootballAIGame.CustomDataTypes.Vector;

public class PlayerAction {
    
    public Vector movement;
    
    public Vector kick;
    
    public PlayerAction() {
        movement = new Vector();
        kick = new Vector();
    }
}
