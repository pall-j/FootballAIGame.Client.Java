package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.simulationentities.MovableEntity;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Represents the behavior where the player is pursuing the specified movable entity and
 * slows down smoothly as he approaches the entity.
 */
public class Pursuit extends SteeringBehavior {
    
    /**
     * The arrive to the target behavior.
     */
    private Arrive targetArrive;
    
    /**
     * The pursued target.
     */
    public MovableEntity target;
    
    /**
     * Initializes a new instance of the {@link Pursuit} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param target The target.
     */
    public Pursuit(Player player, int priority, double weight, MovableEntity target) {
        super(player, priority, weight);
        this.target = target;
        targetArrive = new Arrive(player, priority, weight, target.position);
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
        
        targetArrive.target = target.predictPositionInTime(lookAheadTime);
        
        return targetArrive.getAccelerationVector();
    }
}
