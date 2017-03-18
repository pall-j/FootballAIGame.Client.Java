package FootballAIGame.AI.FSM.UserClasses.PlayerStates.GlobalStates;

import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.KickBall;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.PlayerState;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.PursueBall;

public class FieldPlayerGlobalState extends PlayerState {
    
    private PlayerGlobalState playerGlobalState;
    
    public FieldPlayerGlobalState(Player player, Ai ai) {
        super(player, ai);
        playerGlobalState = new PlayerGlobalState(player, ai);
    }
    
    @Override
    public void run() {
        
        Team team = ai.myTeam;
        
        if (player.canKickBall(ai.ball)) {
            player.stateMachine.changeState(new KickBall(player, ai));
        } else if (team.getNearestPlayerToBall() == player &&
                team.passReceiver == null) {
            player.stateMachine.changeState(new PursueBall(player, ai));
        }
        
        playerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        return playerGlobalState.processMessage(message);
    }
}
