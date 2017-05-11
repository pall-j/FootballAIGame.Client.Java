package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Interpose;
import com.footballaigame.client.ais.fsm.teamstates.Defending;

/**
 * Represents the player's defend goal state. The player will interpose
 * himself between the ball and the controlling player in accordance with the
 * {@link Parameters#DEFEND_GOAL_DISTANCE}. If the player can intercept
 * the ball in accordance with the {@link Parameters#GOALKEEPER_INTERCEPT_RANGE},
 * he will go to {@link InterceptBall} state. Used by the goalkeeper.
 */
public class DefendGoal extends PlayerState {
    
    /**
     * The interpose that is used to move between the ball and the controlling
     * player from any team.
     */
    private Interpose interpose;
    
    /**
     * Initializes a new instance of the {@link DefendGoal} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public DefendGoal(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        Vector goalCenter = new Vector(0, GameClient.FIELD_HEIGHT / 2);
        if (!fsmAI.myTeam.isOnLeft)
            goalCenter.x = GameClient.FIELD_WIDTH;
        
        interpose = new Interpose(player, 1, 1.0, fsmAI.ball, goalCenter);
        interpose.preferredDistanceFromSecond = Parameters.DEFEND_GOAL_DISTANCE;
        
        player.steeringBehaviorsManager.addBehavior(interpose);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
    @Override
    public void run() {
        if (fsmAI.myTeam.stateMachine.currentState instanceof Defending &&
                Vector.getDistanceBetween(fsmAI.ball.position, fsmAI.myTeam.getGoalCenter()) <
                        Parameters.GOALKEEPER_INTERCEPT_RANGE) {
            player.stateMachine.changeState(new InterceptBall(player, fsmAI));
        }
    }
    
    /**
     * Occurs when the entity leaves this state.
     */
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(interpose);
    }
}
