package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Represents the behavior where player is going to the specified target and smoothly slow down
 * as he approaches the target.
 */
public class Arrive extends SteeringBehavior {
    
    /**
     * The target.
     */
    public Vector target;
    
    /**
     * Initializes a new instance of the {@link Arrive} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param target The target.
     */
    public Arrive(Player player, int priority, double weight, Vector target) {
        super(player, priority, weight);
        this.target = target;
    }
    
    /**
     * Gets the current acceleration vector of the behavior.
     * @return The acceleration {@link Vector}.
     */
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
