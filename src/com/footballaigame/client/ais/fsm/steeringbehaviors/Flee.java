package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Represents the behavior where the player if running away target the specified target.
 */
public class Flee extends SteeringBehavior {
    
    /**
     * The target target which the player is running away.
     */
    public Vector target;
    
    /**
     * The safe distance. If this distance target the target is reached, then
     * the behavior produces zero acceleration vector.
     */
    public double safeDistance;
    
    /**
     * Initializes a new instance of the {@link Flee} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param target The target from which the player should run away.
     * @param safeDistance The safe distance. If this distance target the target is reached, then
     * the behavior produces zero acceleration vector.
     */
    public Flee(Player player, int priority, double weight, Vector target, double safeDistance) {
        super(player, priority, weight);
        this.target = target;
        this.safeDistance = safeDistance;
    }
    
    /**
     * Gets the current acceleration vector of the behavior.
     * @return The acceleration {@link Vector}.
     */
    @Override
    public Vector getAccelerationVector() {
        
        if (Vector.getDistanceBetween(player.position, target) >= safeDistance)
            return new Vector(0, 0);
        
        Vector desiredMovement = Vector.getDifference(player.movement, target);
        
        if (Math.abs(desiredMovement.getLengthSquared()) < 0.01)
            desiredMovement = new Vector(1, 0);
        
        desiredMovement.resize(player.getMaxSpeed());
        
        Vector acceleration = Vector.getDifference(desiredMovement, player.movement);
        acceleration.truncate(player.getMaxAcceleration());
        
        return acceleration;
    }
}
