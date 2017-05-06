package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Ball;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Arrive;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Pursuit;
import com.footballaigame.client.ais.fsm.steeringbehaviors.SteeringBehavior;

public class ReceivePass extends PlayerState {
    
    private SteeringBehavior steeringBehavior;
    
    private Vector passTarget;
    
    public ReceivePass(Player player, FsmAI fsmAI, Vector passTarget) {
        super(player, fsmAI);
        this.passTarget = passTarget;
    }
    
    @Override
    public void enter() {
        fsmAI.myTeam.passReceiver = player;
        fsmAI.myTeam.controllingPlayer = player;
        steeringBehavior = new Arrive(player, 1, 1.0, passTarget);
        player.steeringBehaviorsManager.addBehavior(steeringBehavior);
    }
    
    @Override
    public void run() {
        if (fsmAI.myTeam.passReceiver != player) {
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        // lost control
        if (fsmAI.opponentTeam.playerInBallRange != null && fsmAI.myTeam.playerInBallRange == null) {
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        if (player.canKickBall(fsmAI.ball)) {
            player.stateMachine.changeState(new KickBall(player, fsmAI));
            return;
        }
        
        if (Vector.getDistanceBetween(fsmAI.ball.position, player.position) < Parameters.BALL_RECEIVING_RANGE) {
            player.stateMachine.changeState(new PursueBall(player, fsmAI));
            return;
        }
        
        updatePassTarget();
        
        Player nearestOpponent = fsmAI.opponentTeam.getNearestPlayerToPosition(player.position);
        Ball ball = fsmAI.ball;
        
        double timeToReceive = ball.getTimeToCoverDistance(Vector.getDistanceBetween(ball.position, passTarget), ball.getCurrentSpeed());
        
        if (nearestOpponent.getTimeToGetToTarget(passTarget) < timeToReceive ||
                player.getTimeToGetToTarget(passTarget) > timeToReceive) {
            if (steeringBehavior instanceof Arrive) {
                player.steeringBehaviorsManager.removeBehavior(steeringBehavior);
                steeringBehavior = new Pursuit(player, steeringBehavior.priority, steeringBehavior.weight, ball);
                player.steeringBehaviorsManager.addBehavior(steeringBehavior);
            }
        } else {
            if (steeringBehavior instanceof Pursuit) {
                player.steeringBehaviorsManager.removeBehavior(steeringBehavior);
                steeringBehavior = new Arrive(player, steeringBehavior.priority, steeringBehavior.weight, passTarget);
                player.steeringBehaviorsManager.addBehavior(steeringBehavior);
            }
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(steeringBehavior);
        if (player == fsmAI.myTeam.passReceiver)
            fsmAI.myTeam.passReceiver = null;
    }
    
    private void updatePassTarget() {
        Ball ball = fsmAI.ball;
        double time = ball.getTimeToCoverDistance(Vector.getDistanceBetween(passTarget, ball.position), ball.getCurrentSpeed());
        
        passTarget = ball.predictPositionInTime(time);
    
        if (steeringBehavior instanceof Arrive)
            ((Arrive)steeringBehavior).target = passTarget;
    }
}
