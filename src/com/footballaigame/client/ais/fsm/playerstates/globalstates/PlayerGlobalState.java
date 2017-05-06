package com.footballaigame.client.ais.fsm.playerstates.globalstates;

import com.footballaigame.client.ais.fsm.messaging.messages.*;
import com.footballaigame.client.ais.fsm.playerstates.*;
import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Ball;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;

public class PlayerGlobalState extends PlayerState {
    
    protected PlayerGlobalState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void run() {
        
    }
    
    @Override
    public boolean processMessage(Message message) {
        
        if (message instanceof ReturnToHomeMessage) {
            player.stateMachine.changeState(new MoveToHomeRegion(player, fsmAI));
            return true;
        }
        
        if (message instanceof SupportControllingMessage) {
            if (!(player.stateMachine.currentState instanceof SupportControlling))
                player.stateMachine.changeState(new SupportControlling(player, fsmAI));
            return true;
        }
        
        if (message instanceof GoDefaultMessage) {
            player.stateMachine.changeState(new Default(player, fsmAI));
            return true;
        }
        
        if (message instanceof PassToPlayerMessage) {
            
            Ball ball = fsmAI.ball;
            Player target = ((PassToPlayerMessage) message).receiver;
            
            double time = ball.getTimeToCoverDistance(Vector.getDistanceBetween(target.position, ball.position),
                    player.getMaxKickSpeed());
            
            if (Double.isInfinite(time)) // pass not possible
                return true;
            
            Vector predictedTargetPosition = target.predictPositionInTime(time);
            
            if (player.canKickBall(ball)) {
                player.kickBall(ball, predictedTargetPosition);
                MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(predictedTargetPosition));
                player.stateMachine.changeState(new Default(player, fsmAI));
            }
            
            return true;
        }
        
        if (message instanceof ReceivePassMessage) {
            ReceivePassMessage msg = (ReceivePassMessage) message;
            player.stateMachine.changeState(new ReceivePass(player, fsmAI, msg.PassTarget));
            return true;
        }
        
        if (message instanceof PursueBallMessage) {
            player.stateMachine.changeState(new PursueBall(player, fsmAI));
            return true;
        }
        
        return false;
    }
}
