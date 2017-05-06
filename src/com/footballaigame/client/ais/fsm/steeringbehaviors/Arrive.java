package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

public class Arrive extends SteeringBehavior {
    
    public Vector target;
    
    public Arrive(Player player, int priority, double weight, Vector target) {
        super(player, priority, weight);
        this.target = target;
    }
    
    @Override
    public Vector getAccelerationVector() {
        
        double distance = Vector.getDistanceBetween(player.position, target);
        
        Vector desiredMovement = Vector.getDifference(target, player.position);
        desiredMovement.truncate(player.getMaxSpeed());
        
        Vector acceleration = Vector.getDifference(desiredMovement, player.movement);
        acceleration.truncate(player.getMaxAcceleration());
        
        desiredMovement = Vector.getSum(player.movement, acceleration);
        double speed = desiredMovement.getLength();
        
        // calculation (k == 0 -> next step will be stop)
        double v0 = desiredMovement.getLength();
        double v1 = 0;
        double a = -player.getMaxAcceleration();
        double k = Math.floor((v1 - v0) / a);
        if (v0 > 0 && v0 > -a && distance - desiredMovement.getLength() < k * v0 + a * k / 2.0 * (1 + k))
            speed = Math.max(0, player.getCurrentSpeed() + a);
        
        speed = Math.min(speed, player.getMaxSpeed());
        
        if (desiredMovement.getLength() > 0.001)
            desiredMovement.resize(speed);
        else
            desiredMovement = new Vector(0, 0);
        
        acceleration = Vector.getDifference(desiredMovement, player.movement);
        if (acceleration.getLength() > player.getMaxAcceleration())
            acceleration.resize(player.getMaxAcceleration());
        
        return acceleration;
    }
}
