package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.GoalKeeper;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.PassToPlayerMessage;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Wander;

public class Default extends PlayerState {
    
    private Wander wander;
    
    public Default(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        
        wander = new Wander(player, 1, 0.2, 0, 2, 4);
        
        player.steeringBehaviorsManager.addBehavior(wander);
    }
    
    @Override
    public void run() {
        Player controlling = fsmAI.myTeam.controllingPlayer;
        Team team = fsmAI.myTeam;
        
        if (player instanceof GoalKeeper) {
            player.stateMachine.changeState(new DefendGoal(player, fsmAI));
            return;
        }
        
        if (controlling != null &&
                team.isNearerToOpponent(player, controlling) &&
                team.isPassFromControllingSafe(player.position) &&
                team.passReceiver == null) {
            MessageDispatcher.getInstance().sendMessage(new PassToPlayerMessage(player), controlling);
        } else if (!player.isAtHomeRegion()) {
            player.stateMachine.changeState(new MoveToHomeRegion(player, fsmAI));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(wander);
    }
}
