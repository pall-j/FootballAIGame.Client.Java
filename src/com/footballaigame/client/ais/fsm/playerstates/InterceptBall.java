package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Pursuit;
import com.footballaigame.client.ais.fsm.teamstates.Attacking;

/**
 * Represents the player's intercept state. Player in this state tries to intercept the
 * ball until he is in {@link Parameters#GOALKEEPER_INTERCEPT_RANGE} distance from
 * his goal or while his team is attacking. Used by the goalkeeper.
 */
public class InterceptBall extends PlayerState {
    
    /**
     * The ball's pursuit.
     */
    private Pursuit ballPursuit;
    
    /**
     * Initializes a new instance of the {@link InterceptBall} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public InterceptBall(Player player, FsmAI fsmAI) {
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
        if (fsmAI.myTeam.stateMachine.currentState instanceof Attacking ||
                Vector.getDistanceBetween(player.position, fsmAI.myTeam.getGoalCenter()) >
                        Parameters.GOALKEEPER_INTERCEPT_RANGE) {
            player.stateMachine.changeState(new DefendGoal(player, fsmAI));
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
