package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.GoalKeeper;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.PassToPlayerMessage;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Wander;

/**
 * Represents the player's default state. Its the initial state of all players.
 */
public class Default extends PlayerState {
    
    /**
     * The wander.
     */
    private Wander wander;
    
    /**
     * Initializes a new instance of the {@link Default} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public Default(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        
        wander = new Wander(player, 1, 0.2, 0, 2, 4);
        
        player.steeringBehaviorsManager.addBehavior(wander);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
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
    
    /**
     * Occurs when the entity leaves this state.
     */
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(wander);
    }
}
