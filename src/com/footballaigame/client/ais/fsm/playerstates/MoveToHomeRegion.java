package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Arrive;

/**
 * Represents the player's move to home state. Player in this state
 * runs to his home region.
 */
public class MoveToHomeRegion extends PlayerState {
    
    /**
     * The move to home region arrive behavior.
     */
    private Arrive moveToHomeRegionArrive;
    
    /**
     * Initializes a new instance of the {@link MoveToHomeRegion} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public MoveToHomeRegion(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        moveToHomeRegionArrive = new Arrive(player, 3, 1, player.homeRegion.center);
        player.steeringBehaviorsManager.addBehavior(moveToHomeRegionArrive);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
    @Override
    public void run() {
        moveToHomeRegionArrive.target = player.homeRegion.center;
        if (player.isAtHomeRegion() && Math.abs(player.getCurrentSpeed()) < 0.00001)
            player.stateMachine.changeState(new Default(player, fsmAI));
    }
    
    /**
     * Occurs when the entity leaves this state.
     */
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(moveToHomeRegionArrive);
    }
}
