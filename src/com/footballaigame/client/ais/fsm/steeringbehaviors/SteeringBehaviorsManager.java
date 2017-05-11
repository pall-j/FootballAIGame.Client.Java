package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Responsible for managing player's steering behaviors.
 * Provides methods for adding and removing steering behavior of the player.
 * Combines the behaviors in accordance with their priority and weight.
 */
public class SteeringBehaviorsManager {
    
    /**
     * The player to whom this instance belongs..
     */
    private Player player;
    
    /**
     * The {@link TreeMap} that holds the active steering behaviors with keys
     * equal to the behaviors' priorities.
     */
    private TreeMap<Integer, List<SteeringBehavior>> steeringBehaviors;
    
    /**
     *Initializes a new instance of the {@link SteeringBehaviorsManager} class.
     * @param player The player.
     */
    public SteeringBehaviorsManager(Player player) {
        steeringBehaviors = new TreeMap<Integer, List<SteeringBehavior>>();
        this.player = player;
    }
    
    /**
     * Adds the specified behavior.
     * @param behavior The behavior.
     */
    public void addBehavior(SteeringBehavior behavior) {
        List<SteeringBehavior> list;
        list = steeringBehaviors.get(behavior.priority);
        
        if (list != null)
            list.add(behavior);
        else {
            list = new LinkedList<SteeringBehavior>();
            list.add(behavior);
            steeringBehaviors.put(behavior.priority, list);
        }
    }
    
    /**
     * Removes the specified behavior.
     * @param behavior The behavior.
     */
    public void removeBehavior(SteeringBehavior behavior) {
        List<SteeringBehavior> list = steeringBehaviors.get(behavior.priority);
        if (list != null)
            list.remove(behavior);
    }
    
    /**
     * Removes all behaviors of the specified type.
     * @param typeClass The type class.
     * @param <T> The type.
     */
    public <T> void removeAllBehaviorsOfType(Class<T> typeClass) {
        for (Map.Entry<Integer, List<SteeringBehavior>> entry : steeringBehaviors.entrySet()) {
            
            List<SteeringBehavior> list = entry.getValue();
            LinkedList<SteeringBehavior> steeringBehaviorsToBeRemoved = new LinkedList<SteeringBehavior>();
            
            for (SteeringBehavior steeringBehavior : list) {
                if (steeringBehavior.getClass() == typeClass) {
                    steeringBehaviorsToBeRemoved.add(steeringBehavior);
                }
            }
            
            list.removeAll(steeringBehaviorsToBeRemoved);
        }
    }
    
    /**
     * Gets all behaviors of the specified type.
     * @param typeClass The type class.
     * @param <T> The type.
     * @return {@link List} of all active {@link SteeringBehavior}s of the specified type.
     */
    public <T> List<SteeringBehavior> getAllBehaviorsOfType(Class<T> typeClass) {
        List<SteeringBehavior> result = new LinkedList<SteeringBehavior>();
        
        for (Map.Entry<Integer, List<SteeringBehavior>> entry : steeringBehaviors.entrySet()) {
            List<SteeringBehavior> list = entry.getValue();
            
            for (SteeringBehavior steeringBehavior : list) {
                if (steeringBehavior.getClass() == typeClass) {
                    result.add(steeringBehavior);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Gets the current acceleration vector by combining the active behaviors accordingly.
     * @return The acceleration {@link Vector}.
     */
    public Vector getAccelerationVector() {
        
        // Weighted Prioritized Truncated Sum method used
        
        Vector acceleration = new Vector(0, 0);
        double accelerationRemaining = player.getMaxAcceleration();
        
        for (Map.Entry<Integer, List<SteeringBehavior>> keyValuePair : steeringBehaviors.entrySet()) {
            List<SteeringBehavior> list = keyValuePair.getValue();
            
            for (SteeringBehavior steeringbehavior : list) {
                Vector behaviorAcceleration = steeringbehavior.getAccelerationVector();
                behaviorAcceleration.multiply(steeringbehavior.weight);
                
                if (accelerationRemaining - behaviorAcceleration.getLength() < 0)
                    behaviorAcceleration.resize(accelerationRemaining);
                accelerationRemaining -= behaviorAcceleration.getLength();
                
                acceleration = Vector.getSum(acceleration, behaviorAcceleration);
                
                if (accelerationRemaining <= 0)
                    break;
            }
            
            if (accelerationRemaining <= 0)
                break;
        }
    
        Vector nextMovement = Vector.getSum(player.movement, acceleration);
        nextMovement.truncate(player.getMaxSpeed());
        acceleration = Vector.getDifference(nextMovement, player.movement);
        
        return acceleration;
    }
    
    /**
     * Removes all active behaviors.
     */
    public void reset() {
        steeringBehaviors = new TreeMap<Integer, List<SteeringBehavior>>();
    }
    
}
