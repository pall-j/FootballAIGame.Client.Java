package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.GoalKeeper;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.PassToPlayerMessage;
import com.footballaigame.client.ais.fsm.messaging.messages.SupportControllingMessage;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Arrive;

/**
 * Represents the player's support controlling state. The player in this state
 * supports the controlling player by moving to the best support position.
 * If he is able to shot on goal from that position, then he
 * requests the pass from the controlling player. If there is some other team's
 * player nearer to the best support position, then that player state is changed
 * to this state and the player will go to {@link Default} state.
 */
public class SupportControlling extends PlayerState {
    
    /**
     * The arrive behavior that is used to move to the best support position.
     */
    private Arrive arrive;
    
    /**
     * Initializes a new instance of the {@link SupportControlling} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public SupportControlling(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        arrive = new Arrive(player, 1, 1.0, fsmAI.supportPositionsManager.getBestSupportPosition());
        player.steeringBehaviorsManager.addBehavior(arrive);
        fsmAI.myTeam.supportingPlayers.add(player);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
    @Override
    public void run() {
        arrive.target = fsmAI.supportPositionsManager.getBestSupportPosition();
        Team team = fsmAI.myTeam;
        
        // nearest except goalkeeper and controlling
        Player nearest = fsmAI.myTeam.getNearestPlayerToPosition(arrive.target, team.goalKeeper, team.controllingPlayer);
        
        // goalkeeper shouldn't go too far target his home region
        if (player instanceof GoalKeeper &&
                Vector.getDistanceBetween(arrive.target, player.homeRegion.center) > Parameters.MAX_GOALKEEPER_SUPPORTING_DISTANCE) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        // if shot on goal is possible request pass target controlling
        if (fsmAI.myTeam.tryGetShotOnGoal(player) != null && team.controllingPlayer != null)
            MessageDispatcher.getInstance().sendMessage(new PassToPlayerMessage(player));
        
        // someone else is nearer the best position (not goalkeeper)
        if (!(player instanceof GoalKeeper) && nearest != player && nearest != team.controllingPlayer) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, fsmAI));
        }
    }
    
    /**
     * Occurs when the entity leaves this state.
     */
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(arrive);
        fsmAI.myTeam.supportingPlayers.remove(player);
    }
}
