package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.ReceivePassMessage;

public class KickBall extends PlayerState {
    
    public KickBall(Player player) {
        super(player);
    }
    
    @Override
    public void enter() {
        Ai.getInstance().myTeam.controllingPlayer = player;
        run(); // run immediately
    }
    
    @Override
    public void run() {
        Team team = Ai.getInstance().myTeam;
        
        if (team.passReceiver != null) {
            team.passReceiver = null;
        }
        
        Vector target;
        if ((target = team.tryGetShotOnGoal(player)) != null) {
            player.kickBall(Ai.getInstance().ball, target);
            player.stateMachine.changeState(new Default(player));
            return;
        }
        
        Player passPlayerTarget;
        if (player.isInDanger() && (passPlayerTarget = team.tryGetSafePass(player)) != null) {
            Vector passTarget = player.passBall(Ai.getInstance().ball, passPlayerTarget);
            MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(passTarget), passPlayerTarget);
            player.stateMachine.changeState(new Default(player));
            return;
        }
        
        player.stateMachine.changeState(new Dribble(player));
    }
    
    @Override
    public void exit() {
        if (Ai.getInstance().myTeam.controllingPlayer == player)
            Ai.getInstance().myTeam.controllingPlayer = null;
    }
}
