package FootballAIGame.AI.FSM.UserClasses.Entities;

import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.FiniteStateMachine;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.Default;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.GlobalStates.GoalKeeperGlobalState;

public class GoalKeeper extends Player {
    
    public GoalKeeper(FootballPlayer player, Ai ai) {
        super(player, ai);
        
        stateMachine = new FiniteStateMachine<Player>(this, new Default(this, ai),
                new GoalKeeperGlobalState(this, ai));
    }
    
    @Override
    public void processMessage(Message message) {
        stateMachine.processMessage(this, message);
    }
}
