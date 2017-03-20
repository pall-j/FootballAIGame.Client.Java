package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.GoalKeeper;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.PursueBallMessage;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Pursuit;

public class PursueBall extends PlayerState {
    
    private Pursuit ballPursuit;
    
    public PursueBall(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        ballPursuit = new Pursuit(player, 1, 1.0, fsmAI.ball);
        player.steeringBehaviorsManager.addBehavior(ballPursuit);
    }
    
    @Override
    public void run() {
        if (player.canKickBall(fsmAI.ball)) {
            player.stateMachine.changeState(new KickBall(player, fsmAI));
            return;
        }
        
        Player nearestToBall = fsmAI.myTeam.getNearestPlayerToBall();
        if (player != nearestToBall && !(nearestToBall instanceof GoalKeeper)) {
            player.stateMachine.changeState(new MoveToHomeRegion(player, fsmAI));
            MessageDispatcher.getInstance().sendMessage(new PursueBallMessage(), nearestToBall);
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(ballPursuit);
    }
}
