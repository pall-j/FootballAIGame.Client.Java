package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Arrive;

public class MoveToHomeRegion extends PlayerState {
    
    private Arrive moveToHomeRegionArrive;
    
    public MoveToHomeRegion(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        moveToHomeRegionArrive = new Arrive(player, 3, 1, player.homeRegion.center);
        player.steeringBehaviorsManager.addBehavior(moveToHomeRegionArrive);
    }
    
    @Override
    public void run() {
        moveToHomeRegionArrive.target = player.homeRegion.center;
        if (player.isAtHomeRegion() && Math.abs(player.getCurrentSpeed()) < 0.00001)
            player.stateMachine.changeState(new Default(player, fsmAI));
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(moveToHomeRegionArrive);
    }
}
