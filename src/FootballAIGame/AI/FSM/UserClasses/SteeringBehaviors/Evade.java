package FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.SimulationEntities.MovableEntity;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;

public class Evade extends SteeringBehavior {
    
    private Flee fleeFromTarget;
    
    public MovableEntity target;
    
    public double getSafeDistance() {
        return fleeFromTarget.safeDistance;
    }
    
    public void setSafeDistance(double value) {
        fleeFromTarget.safeDistance = value;
    }
    
    public Evade(Player player, int priority, double weight, MovableEntity target, double safeDistance) {
        super(player, priority, weight);
        this.target = target;
        fleeFromTarget = new Flee(player, priority, weight, target.position, safeDistance);
    }
    
    @Override
    public Vector calculateAccelerationVector() {
        
        double distance = Vector.distanceBetween(player.position, target.position);
        
        double lookAheadTime = 0;
        if (player.currentSpeed() + target.currentSpeed() > 0)
            lookAheadTime = distance / (player.currentSpeed() + target.currentSpeed());
        
        Vector predictedPosition = Vector.sum(target.position,
                target.movement.getMultiplied(lookAheadTime));
        
        fleeFromTarget.from = predictedPosition;
        
        return fleeFromTarget.calculateAccelerationVector();
    }
}
