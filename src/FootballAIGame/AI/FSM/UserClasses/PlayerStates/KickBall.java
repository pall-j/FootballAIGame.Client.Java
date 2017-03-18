package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.ReceivePassMessage;

public class KickBall extends PlayerState {
    
    public KickBall(Player player, Ai ai) {
        super(player, ai);
    }
    
    @Override
    public void enter() {
        ai.myTeam.controllingPlayer = player;
        run(); // run immediately
    }
    
    @Override
    public void run() {
        Team team = ai.myTeam;
        
        if (team.passReceiver != null) {
            team.passReceiver = null;
        }
        
        Vector target;
        if ((target = team.tryGetShotOnGoal(player)) != null) {
            player.kickBall(ai.ball, target);
            player.stateMachine.changeState(new Default(player, ai));
            return;
        }
        
        Player passPlayerTarget;
        if (player.isInDanger() && (passPlayerTarget = team.tryGetSafePass(player)) != null) {
            Vector passTarget = player.passBall(ai.ball, passPlayerTarget);
            MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(passTarget), passPlayerTarget);
            player.stateMachine.changeState(new Default(player, ai));
            return;
        }
        
        player.stateMachine.changeState(new Dribble(player, ai));
    }
    
    @Override
    public void exit() {
        if (ai.myTeam.controllingPlayer == player)
            ai.myTeam.controllingPlayer = null;
    }
}
