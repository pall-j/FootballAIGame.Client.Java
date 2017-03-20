package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.ReceivePassMessage;

public class KickBall extends PlayerState {
    
    public KickBall(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        fsmAI.myTeam.controllingPlayer = player;
        run(); // run immediately
    }
    
    @Override
    public void run() {
        Team team = fsmAI.myTeam;
        
        if (team.passReceiver != null) {
            team.passReceiver = null;
        }
        
        Vector target;
        if ((target = team.tryGetShotOnGoal(player)) != null) {
            player.kickBall(fsmAI.ball, target);
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        Player passPlayerTarget;
        if (player.isInDanger() && (passPlayerTarget = team.tryGetSafePass(player)) != null) {
            Vector passTarget = player.passBall(fsmAI.ball, passPlayerTarget);
            MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(passTarget), passPlayerTarget);
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        player.stateMachine.changeState(new Dribble(player, fsmAI));
    }
    
    @Override
    public void exit() {
        if (fsmAI.myTeam.controllingPlayer == player)
            fsmAI.myTeam.controllingPlayer = null;
    }
}
