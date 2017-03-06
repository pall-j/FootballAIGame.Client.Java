package FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;

public class Arrive extends SteeringBehavior {
    
    public Vector target;
    
    public Arrive(Player player, int priority, double weight, Vector target) {
        super(player, priority, weight);
        this.target = target;
    }
    
    @Override
    public Vector calculateAccelerationVector() {
        
        double distance = Vector.distanceBetween(player.position, target);
        
        Vector desiredMovement = Vector.difference(target, player.position);
        desiredMovement.truncate(player.maxSpeed());
        
        Vector acceleration = Vector.difference(desiredMovement, player.movement);
        acceleration.truncate(player.maxAcceleration());
        
        desiredMovement = Vector.sum(player.movement, acceleration);
        double speed = desiredMovement.length();
        
        // calculation (k == 0 -> next step will be stop)
        double v0 = desiredMovement.length();
        double v1 = 0;
        double a = -player.maxAcceleration();
        double k = Math.floor((v1 - v0) / a);
        if (v0 > 0 && v0 > -a && distance - desiredMovement.length() < k * v0 + a * k / 2.0 * (1 + k))
            speed = Math.max(0, player.currentSpeed() + a);
        
        speed = Math.min(speed, player.maxSpeed());
        
        if (desiredMovement.length() > 0.001)
            desiredMovement.resize(speed);
        else
            desiredMovement = new Vector(0, 0);
        
        acceleration = Vector.difference(desiredMovement, player.movement);
        if (acceleration.length() > player.maxAcceleration())
            acceleration.resize(player.maxAcceleration());
        
        return acceleration;
    }
}
