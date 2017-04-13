package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.GoalKeeper;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.PursueBallMessage;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Pursuit;

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
