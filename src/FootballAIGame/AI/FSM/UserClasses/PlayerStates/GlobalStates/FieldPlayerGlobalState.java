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
    
    public FieldPlayerGlobalState(Player player) {
        super(player);
        playerGlobalState = new PlayerGlobalState(player);
    }
    
    @Override
    public void run() {
        
        Team team = Ai.getInstance().myTeam;
        
        if (player.canKickBall(Ai.getInstance().ball)) {
            player.stateMachine.changeState(new KickBall(player));
        } else if (team.getNearestPlayerToBall() == player &&
                team.passReceiver == null) {
            player.stateMachine.changeState(new PursueBall(player));
        }
        
        playerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        return playerGlobalState.processMessage(message);
    }
}
