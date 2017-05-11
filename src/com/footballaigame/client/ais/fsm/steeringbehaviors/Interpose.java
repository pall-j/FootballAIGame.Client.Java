package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.simulationentities.FootballBall;
import com.footballaigame.client.simulationentities.MovableEntity;
import com.footballaigame.client.ais.fsm.entities.Ball;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Represents the behavior where the player is moving to a position between the specified entities.
 */
public class Interpose extends SteeringBehavior {
    
    /**
     * The first entity.
     */
    public MovableEntity first;
    
    /**
     * The second entity.
     */
    public MovableEntity second;
    
    /**
     * The arrive behavior, that is used to move to a position
     * between the first and the second entity.
     */
    private Arrive arrive;
    
    /**
     * The preferred distance from the second entity.
     */
    public double preferredDistanceFromSecond;
    
    /**
     * Initializes a new instance of the {@link Interpose} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param first The first entity.
     * @param second The second entity.
     */
    public Interpose(Player player, int priority, double weight, MovableEntity first, MovableEntity second) {
        super(player, priority, weight);
        this.first = first;
        this.second = second;
        arrive = new Arrive(player, priority, weight, player.position);
        preferredDistanceFromSecond = Vector.getDistanceBetween(second.position, first.position) / 2.0;
    }
    
    /**
     * Initializes a new instance of the {@link Interpose} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param first The first entity.
     * @param secondPosition The second position that is used instead of entity.
     * The artificial second entity is created at this position.
     */
    public Interpose(Player player, int priority, double weight, MovableEntity first, Vector secondPosition) {
        super(player, priority, weight);
        
        this.first = first;
        this.second = new Ball(new FootballBall()); // artificial movable entity for representing second
        this.second.position = secondPosition;
        
        arrive = new Arrive(player, priority, weight, player.position);
        preferredDistanceFromSecond = Vector.getDistanceBetween(second.position, first.position) / 2.0;
    }
    
    /**
     * Gets the current acceleration vector of the behavior.
     * @return The acceleration {@link Vector}.
     */
    @Override
    public Vector getAccelerationVector() {
        
        Vector firstToSecond = new Vector(first.position, second.position);
        Vector firstToPlayer = new Vector(first.position, player.position);
        
        double firstToTargetDistance = Vector.getDotProduct(firstToPlayer, firstToSecond) / firstToSecond.getLength();
        
        if (firstToTargetDistance < 0 || firstToTargetDistance > firstToSecond.getLength()) {
            arrive.target = Vector.getSum(first.position, firstToSecond.getMultiplied(1 / 2.0)); // go to midpoint
            return arrive.getAccelerationVector();
        }
        
        arrive.target = Vector.getSum(first.position, firstToSecond.getResized(firstToTargetDistance));
        
        double playerToTargetDistance = Vector.getDistanceBetween(arrive.target, player.position);
        
        if (playerToTargetDistance < 0.01 && firstToSecond.getLength() > preferredDistanceFromSecond) {
            // move player to meet DistanceFromSecond condition
            arrive.target = Vector.getSum(first.position, firstToSecond.getResized(firstToSecond.getLength() - preferredDistanceFromSecond));
        }
        
        
        return arrive.getAccelerationVector();
    }
}
