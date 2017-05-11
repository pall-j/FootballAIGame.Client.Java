package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Provides the base class from which the classes that represent steering behaviors are derived.
 */
public class Seek extends SteeringBehavior {
    
    /**
     * The behavior's target.
     */
    public Vector target;
    
    /**
     * Initializes a new instance of the {@link Seek} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param target The target.
     */
    public Seek(Player player, int priority, double weight, Vector target) {
        super(player, priority, weight);
        this.target = target;
    }
    
    /**
     * Gets the current acceleration vector of the behavior.
     * @return The acceleration {@link Vector}.
     */
    @Override
    public Vector getAccelerationVector() {
        Vector acceleration = new Vector(0, 0);
        
        if (target == null) return acceleration;
        
        Vector desiredMovement = Vector.getDifference(target, player.position);
        desiredMovement.truncate(player.getMaxSpeed());
        
        acceleration = Vector.getDifference(desiredMovement, player.movement);
        acceleration.truncate(player.getMaxAcceleration());
        
        return acceleration;
    }
}
