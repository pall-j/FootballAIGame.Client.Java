package FootballAIGame;

import FootballAIGame.SimulationEntities.FootballPlayer;
import FootballAIGame.SimulationEntities.GameAction;
import FootballAIGame.SimulationEntities.GameState;

public interface FootballAI {
    
    void initialize();
    
    GameAction getAction(GameState state);
    
    FootballPlayer[] getParameters();
    
}
