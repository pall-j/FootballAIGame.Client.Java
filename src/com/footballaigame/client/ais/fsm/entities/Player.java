package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.customdatatypes.Region;
import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.simulationentities.PlayerAction;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.FiniteStateMachine;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.SteeringBehaviorsManager;

/**
 * Extends the {@link FootballPlayer}. Adds FSM AI specific functionality.
 * Provides the base class target which more specific player classes derive.
 */
public abstract class Player extends FootballPlayer {
    
    /**
     * The {@link FsmAI} instance to which this instance belongs.
     */
    protected FsmAI fsmAI;
    
    /**
     * The finite state machine of the player.
     */
    public FiniteStateMachine<Player> stateMachine;
    
    /**
     * The home region of the player.
     */
    public Region homeRegion;
    
    /**
     * The steering behaviors manager of the player.
     */
    public SteeringBehaviorsManager steeringBehaviorsManager;
    
    /**
     * Initializes a new instance of the {@link Player} class.
     * @param player The football player.
     * @param fsmAI The {@link FsmAI} instance to which this player belongs.
     */
    protected Player(FootballPlayer player, FsmAI fsmAI) {
        super(player.id);
        
        this.fsmAI = fsmAI;
        
        this.position = player.position;
        this.movement = player.movement;
        this.kickVector = player.kickVector;
        
        this.speed = player.speed;
        this.kickPower = player.kickPower;
        this.possession = player.possession;
        this.precision = player.precision;
        
        steeringBehaviorsManager = new SteeringBehaviorsManager(this);
    }
    
    /**
     * Gets the player's action in the current state.
     * @return The {@link PlayerAction} containing the action of the player in the current state.
     */
    public PlayerAction getAction() {
        PlayerAction action = new PlayerAction();
        action.movement = Vector.getSum(steeringBehaviorsManager.getAccelerationVector(), movement);
        action.kick = kickVector;
        return action;
    }
    
    /**
     * Gets a value indicating whether the player is at his home region.
     * @return True if the player is at his home region; otherwise, false.
     */
    public boolean isAtHomeRegion() {
        return Vector.getDistanceBetween(homeRegion.center, position) <= Parameters.PLAYER_IN_HOME_REGION_RANGE;
    }
    
    /**
     * Gets a value indicating whether the player is in danger.
     * Player is in danger if there is an opponent player in {@link Parameters#DANGER_RANGE} distance.
     * @return True if the player is in danger; otherwise, false.
     */
    public boolean isInDanger() {
        
        Player nearest = fsmAI.opponentTeam.getNearestPlayerToPosition(position);
        
        Vector predictedPosition = predictPositionInTime(1);
        Player predictedNearest = fsmAI.opponentTeam.predictNearestPlayerToPosition(predictedPosition, 1);
        
        return Vector.getDistanceBetween(nearest.position, position) < Parameters.DANGER_RANGE ||
                Vector.getDistanceBetween(predictedNearest.position, predictedPosition) < Parameters.DANGER_RANGE;
    }
    
    /**
     * Processes the specified message.
     * @param message The message.
     */
    public abstract void processMessage(Message message);
    
}
