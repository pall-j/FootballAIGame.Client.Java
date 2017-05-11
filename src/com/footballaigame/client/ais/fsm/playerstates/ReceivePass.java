package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Ball;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Arrive;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Pursuit;
import com.footballaigame.client.ais.fsm.steeringbehaviors.SteeringBehavior;

/**
 * Represents the player's receive pass state. Player in this state goes to the specified pass target location
 * and waits for the ball until the ball is in {@link Parameters#BALL_RECEIVING_RANGE} or he can kick the
 * ball. If the player's team stopped controlling the ball, then goes to {@link Default}.
 * If the player cannot reach the pass target sooner than opponent player or the opponent
 * player would get to ball before it reaches the pass target, then the player
 * pursues the ball.
 */
public class ReceivePass extends PlayerState {
    
    /**
     * The steering behavior used for pursing the ball or arriving at
     * he pass target location.
     */
    private SteeringBehavior steeringBehavior;
    
    /**
     * The pass target.
     */
    private Vector passTarget;
    
    /**
     * Initializes a new instance of the {@link ReceivePass} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public ReceivePass(Player player, FsmAI fsmAI, Vector passTarget) {
        super(player, fsmAI);
        this.passTarget = passTarget;
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        fsmAI.myTeam.passReceiver = player;
        fsmAI.myTeam.controllingPlayer = player;
        steeringBehavior = new Arrive(player, 1, 1.0, passTarget);
        player.steeringBehaviorsManager.addBehavior(steeringBehavior);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
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
    
    /**
     * Occurs when the entity leaves this state.
     */
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(steeringBehavior);
        if (player == fsmAI.myTeam.passReceiver)
            fsmAI.myTeam.passReceiver = null;
    }
    
    /**
     * Updates the pass target to the current predicted position in time
     * in which the ball would cover the distance between its position and the current pass target.
     */
    private void updatePassTarget() {
        Ball ball = fsmAI.ball;
        double time = ball.getTimeToCoverDistance(Vector.getDistanceBetween(passTarget, ball.position), ball.getCurrentSpeed());
        
        passTarget = ball.predictPositionInTime(time);
    
        if (steeringBehavior instanceof Arrive)
            ((Arrive)steeringBehavior).target = passTarget;
    }
}
