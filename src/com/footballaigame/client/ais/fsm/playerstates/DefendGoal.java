package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Interpose;
import com.footballaigame.client.ais.fsm.teamstates.Defending;

public class DefendGoal extends PlayerState {
    
    private Interpose interpose;
    
    public DefendGoal(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        Vector goalCenter = new Vector(0, GameClient.FIELD_HEIGHT / 2);
        if (!fsmAI.myTeam.isOnLeft)
            goalCenter.x = GameClient.FIELD_WIDTH;
        
        interpose = new Interpose(player, 1, 1.0, fsmAI.ball, goalCenter);
        interpose.preferredDistanceFromSecond = Parameters.DEFEND_GOAL_DISTANCE;
        
        player.steeringBehaviorsManager.addBehavior(interpose);
    }
    
    @Override
    public void run() {
        if (fsmAI.myTeam.stateMachine.currentState instanceof Defending &&
                Vector.distanceBetween(fsmAI.ball.position, fsmAI.myTeam.getGoalCenter()) <
                        Parameters.GOALKEEPER_INTERCEPT_RANGE) {
            player.stateMachine.changeState(new InterceptBall(player, fsmAI));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(interpose);
    }
}
