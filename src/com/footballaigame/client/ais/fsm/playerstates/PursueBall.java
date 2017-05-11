package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.GoalKeeper;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.PursueBallMessage;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Pursuit;

/**
 * Represents the player's pursue ball state. Player in this state pursues ball
 * until he reaches it or he is not the nearest field player to ball from his team.
 * If he reaches the ball he will change his state to {@link KickBall}.
 * If he is not the nearest field player to ball from his team anymore, then he changes
 * state to {@link MoveToHomeRegion}.
 */
public class PursueBall extends PlayerState {
    
    /**
     * The ball pursuit.
     */
    private Pursuit ballPursuit;
    
    /**
     * Initializes a new instance of the {@link PursueBall} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public PursueBall(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        ballPursuit = new Pursuit(player, 1, 1.0, fsmAI.ball);
        player.steeringBehaviorsManager.addBehavior(ballPursuit);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
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
    
    /**
     * Occurs when the entity leaves this state.
     */
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(ballPursuit);
    }
}
