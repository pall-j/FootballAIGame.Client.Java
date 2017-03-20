package FootballAIGame.AI.FSM.UserClasses.Entities;

import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;

public abstract class FieldPlayer extends Player {
    
    protected FieldPlayer(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
}
