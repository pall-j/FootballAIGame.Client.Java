package FootballAIGame.AI.FSM.UserClasses.PlayerStates.GlobalStates;

import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.KickBall;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.PlayerState;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.PursueBall;

public class FieldPlayerGlobalState extends PlayerState {
    
    private PlayerGlobalState playerGlobalState;
    
    public FieldPlayerGlobalState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        playerGlobalState = new PlayerGlobalState(player, fsmAI);
    }
    
    @Override
    public void run() {
        
        Team team = fsmAI.myTeam;
        
        if (player.canKickBall(fsmAI.ball)) {
            player.stateMachine.changeState(new KickBall(player, fsmAI));
        } else if (team.getNearestPlayerToBall() == player &&
                team.passReceiver == null) {
            player.stateMachine.changeState(new PursueBall(player, fsmAI));
        }
        
        playerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        return playerGlobalState.processMessage(message);
    }
}
