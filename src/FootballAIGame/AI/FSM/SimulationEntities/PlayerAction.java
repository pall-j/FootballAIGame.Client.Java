package FootballAIGame.AI.FSM.SimulationEntities;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;

/**
 * Represents football player's action that is send to the server.
 */
public class PlayerAction {
    
    /**
     * The desired movement vector of the player.
     */
    public Vector movement;
    
    /**
     * The desired kickVector vector of the player.
     */
    public Vector kick;
    
    /**
     * Initializes a new instance of the {@link PlayerAction} class.
     */
    public PlayerAction() {
        movement = new Vector();
        kick = new Vector();
    }
}
