package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.GoalKeeper;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.PursueBallMessage;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Pursuit;

public class PursueBall extends PlayerState {
    
    private Pursuit ballPursuit;
    
    public PursueBall(Player player, Ai ai) {
        super(player, ai);
    }
    
    @Override
    public void enter() {
        ballPursuit = new Pursuit(player, 1, 1.0, ai.ball);
        player.steeringBehaviorsManager.addBehavior(ballPursuit);
    }
    
    @Override
    public void run() {
        if (player.canKickBall(ai.ball)) {
            player.stateMachine.changeState(new KickBall(player, ai));
            return;
        }
        
        Player nearestToBall = ai.myTeam.getNearestPlayerToBall();
        if (player != nearestToBall && !(nearestToBall instanceof GoalKeeper)) {
            player.stateMachine.changeState(new MoveToHomeRegion(player, ai));
            MessageDispatcher.getInstance().sendMessage(new PursueBallMessage(), nearestToBall);
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(ballPursuit);
    }
}
