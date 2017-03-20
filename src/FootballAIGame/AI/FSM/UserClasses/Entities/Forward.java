package FootballAIGame.AI.FSM.UserClasses.Entities;

import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.FiniteStateMachine;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.Default;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.GlobalStates.ForwardGlobalState;

public class Forward extends FieldPlayer {
    
    public Forward(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
        
        stateMachine = new FiniteStateMachine<Player>(this, new Default(this, fsmAI),
                new ForwardGlobalState(this, fsmAI));
    }
    
    @Override
    public void processMessage(Message message) {
        stateMachine.processMessage(this, message);
    }
}
