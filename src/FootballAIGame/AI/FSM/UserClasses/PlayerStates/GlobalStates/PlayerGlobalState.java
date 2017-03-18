package FootballAIGame.AI.FSM.UserClasses.PlayerStates.GlobalStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Ball;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.*;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.*;

public class PlayerGlobalState extends PlayerState {
    
    protected PlayerGlobalState(Player player, Ai ai) {
        super(player, ai);
    }
    
    @Override
    public void run() {
        
    }
    
    @Override
    public boolean processMessage(Message message) {
        
        if (message instanceof ReturnToHomeMessage) {
            player.stateMachine.changeState(new MoveToHomeRegion(player, ai));
            return true;
        }
        
        if (message instanceof SupportControllingMessage) {
            if (!(player.stateMachine.currentState instanceof SupportControlling))
                player.stateMachine.changeState(new SupportControlling(player, ai));
            return true;
        }
        
        if (message instanceof GoDefaultMessage) {
            player.stateMachine.changeState(new Default(player, ai));
            return true;
        }
        
        if (message instanceof PassToPlayerMessage) {
            
            Ball ball = ai.ball;
            Player target = ((PassToPlayerMessage) message).receiver;
            
            double time = ball.timeToCoverDistance(Vector.distanceBetween(target.position, ball.position),
                    player.maxKickSpeed());
            
            if (Double.isInfinite(time)) // pass not possible
                return true;
            
            Vector predictedTargetPosition = target.predictedPositionInTime(time);
            
            if (player.canKickBall(ball)) {
                player.kickBall(ball, predictedTargetPosition);
                MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(predictedTargetPosition));
                player.stateMachine.changeState(new Default(player, ai));
            }
            
            return true;
        }
        
        if (message instanceof ReceivePassMessage) {
            ReceivePassMessage msg = (ReceivePassMessage) message;
            player.stateMachine.changeState(new ReceivePass(player, ai, msg.PassTarget));
            return true;
        }
        
        if (message instanceof PursueBallMessage) {
            player.stateMachine.changeState(new PursueBall(player, ai));
            return true;
        }
        
        return false;
    }
}
