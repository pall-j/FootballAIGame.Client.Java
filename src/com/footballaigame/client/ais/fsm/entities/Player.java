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

public abstract class Player extends FootballPlayer {
    
    protected FsmAI fsmAI;
    
    public FiniteStateMachine<Player> stateMachine;
    
    public Region homeRegion;
    
    public SteeringBehaviorsManager steeringBehaviorsManager;
    
    public PlayerAction getAction() {
        PlayerAction action = new PlayerAction();
        action.movement = Vector.getSum(steeringBehaviorsManager.getAccelerationVector(), movement);
        action.kick = kickVector;
        return action;
    }
    
    public abstract void processMessage(Message message);
    
    public boolean isAtHomeRegion() {
        return Vector.getDistanceBetween(homeRegion.center, position) <= Parameters.PLAYER_IN_HOME_REGION_RANGE;
    }
    
    public boolean isInDanger() {
        
        Player nearest = fsmAI.opponentTeam.getNearestPlayerToPosition(position);
        
        Vector predictedPosition = predictPositionInTime(1);
        Player predictedNearest = fsmAI.opponentTeam.predictNearestPlayerToPosition(predictedPosition, 1);
        
        return Vector.getDistanceBetween(nearest.position, position) < Parameters.DANGER_RANGE ||
                Vector.getDistanceBetween(predictedNearest.position, predictedPosition) < Parameters.DANGER_RANGE;
    }
    
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
    
}
