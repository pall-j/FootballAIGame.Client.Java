package FootballAIGame.AI.FSM.UserClasses.Entities;

import FootballAIGame.AI.FSM.CustomDataTypes.Region;
import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.SimulationEntities.PlayerAction;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.FiniteStateMachine;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;
import FootballAIGame.AI.FSM.UserClasses.Parameters;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.SteeringBehaviorsManager;

public abstract class Player extends FootballPlayer {
    
    protected Ai ai;
    
    public FiniteStateMachine<Player> stateMachine;
    
    public Region homeRegion;
    
    public SteeringBehaviorsManager steeringBehaviorsManager;
    
    public PlayerAction getAction() {
        PlayerAction action = new PlayerAction();
        action.movement = Vector.sum(steeringBehaviorsManager.calculateAccelerationVector(), movement);
        action.kick = kickVector;
        return action;
    }
    
    public abstract void processMessage(Message message);
    
    public boolean isAtHomeRegion() {
        return Vector.distanceBetween(homeRegion.center, position) <= Parameters.PLAYER_IN_HOME_REGION_RANGE;
    }
    
    public boolean isInDanger() {
        
        Player nearest = ai.opponentTeam.getNearestPlayerToPosition(position);
        
        Vector predictedPosition = predictedPositionInTime(1);
        Player predictedNearest = ai.opponentTeam.getPredictedNearestPlayerToPosition(predictedPosition, 1);
        
        return Vector.distanceBetween(nearest.position, position) < Parameters.DANGER_RANGE ||
                Vector.distanceBetween(predictedNearest.position, predictedPosition) < Parameters.DANGER_RANGE;
    }
    
    protected Player(FootballPlayer player, Ai ai) {
        super(player.id);
        
        this.ai = ai;
        
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
