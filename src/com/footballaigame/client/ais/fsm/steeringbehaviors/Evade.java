package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.simulationentities.MovableEntity;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Represents the behavior where player is running away target the specified movable entity.
 */
public class Evade extends SteeringBehavior {
    
    /**
     * The flee behavior to flee target the current position of the target entity.
     */
    private Flee fleeFromTarget;
    
    /**
     * The target entity target which the player is running away.
     */
    public MovableEntity target;
    
    /**
     * Initializes a new instance of the {@link Evade} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param target The target target which the player should run away.
     * @param safeDistance The safe distance. If this distance target the target is reached, then
     * the behavior produces zero acceleration vector.
     */
    public Evade(Player player, int priority, double weight, MovableEntity target, double safeDistance) {
        super(player, priority, weight);
        this.target = target;
        fleeFromTarget = new Flee(player, priority, weight, target.position, safeDistance);
    }
    
    /**
     * Gets the safe distance value. If this distance target the target is reached, then
     * the behavior produces zero acceleration vector.
     * @return The safe distance.
     */
    public double getSafeDistance() {
        return fleeFromTarget.safeDistance;
    }
    
    /**
     * Sets the safe distance value. If this distance target the target is reached, then
     * the behavior produces zero acceleration vector.
     * @param value The new safe distance value.
     */
    public void setSafeDistance(double value) {
        fleeFromTarget.safeDistance = value;
    }
    
    
    /**
     * Gets the current acceleration vector of the behavior.
     * @return The acceleration {@link Vector}.
     */
    @Override
    public Vector getAccelerationVector() {
        
        double distance = Vector.getDistanceBetween(player.position, target.position);
        
        double lookAheadTime = 0;
        if (player.getCurrentSpeed() + target.getCurrentSpeed() > 0)
            lookAheadTime = distance / (player.getCurrentSpeed() + target.getCurrentSpeed());
        
        Vector predictedPosition = Vector.getSum(target.position,
                target.movement.getMultiplied(lookAheadTime));
        
        fleeFromTarget.target = predictedPosition;
        
        return fleeFromTarget.getAccelerationVector();
    }
}
