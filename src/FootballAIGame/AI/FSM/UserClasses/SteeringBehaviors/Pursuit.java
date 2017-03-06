package FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.SimulationEntities.MovableEntity;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;

public class Pursuit extends SteeringBehavior {
    
    private Arrive targetArrive;
    
    public MovableEntity target;
    
    public Pursuit(Player player, int priority, double weight, MovableEntity target) {
        super(player, priority, weight);
        this.target = target;
        targetArrive = new Arrive(player, priority, weight, target.position);
    }
    
    @Override
    public Vector calculateAccelerationVector() {
        double distance = Vector.distanceBetween(player.position, target.position);
        
        double lookAheadTime = 0;
        if (player.currentSpeed() + target.currentSpeed() > 0)
            lookAheadTime = distance / (player.currentSpeed() + target.currentSpeed());
        
        targetArrive.target = target.predictedPositionInTime(lookAheadTime);
        
        return targetArrive.calculateAccelerationVector();
    }
}
