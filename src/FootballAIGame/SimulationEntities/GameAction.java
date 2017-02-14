package FootballAIGame.SimulationEntities;

/**
 * Represents the AI action that consists of the football players actions.
 */
public class GameAction {
    
    /**
     * The player's actions. When this instance is sent to the server, there should be 11
     * players with their movement and kick vectors set.
     */
    public PlayerAction[] playerActions;
    
    /**
     * The simulation step of the action. Describes to which simulation step this action belongs.
     */
    public int step;
    
}
