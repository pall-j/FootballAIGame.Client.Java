package FootballAIGame.AI.FSM.UserClasses.Entities;

import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.UserClasses.Ai;

public abstract class FieldPlayer extends Player {
    
    protected FieldPlayer(FootballPlayer player, Ai ai) {
        super(player, ai);
    }
}
