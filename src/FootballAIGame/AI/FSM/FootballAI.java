package FootballAIGame.AI.FSM;

import FootballAIGame.AI.FSM.SimulationEntities.GameState;
import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.SimulationEntities.GameAction;

public interface FootballAI {
    
    /**
     * Called every time the new match simulation with the AI starts.
     * <p>
     * Called before {@link #getParameters()}.
     */
    void initialize();
    
    /**
     * Gets the {@link GameAction} for the specified {@link GameState}.
     *
     * @param state State of the game.
     * @return The {@link GameAction} for the specified {@link GameState}.
     */
    GameAction getAction(GameState state);
    
    /**
     * Gets the player parameters. position and moving vector properties are ignored.
     *
     * @return The array of football players with their parameters set.
     */
    FootballPlayer[] getParameters();
    
}
