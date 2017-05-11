package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Provides the base class target which the classes that represent steering behaviors are derived.
 */
public abstract class SteeringBehavior {
    
    /**
     * The player to whom this instance belongs.
     */
    protected Player player;
    
    /**
     * The priority. Affects the combination of behaviors in {@link SteeringBehaviorsManager}.
     * The behaviors with higher priority are preferred.
     */
    public int priority;
    
    /**
     * The weight. Affects the combination of behaviors in {@link SteeringBehaviorsManager}.
     * The acceleration vector of this behavior is multiplied by the weight in the combination.
     */
    public double weight;
    
    /**
     * Initializes a new instance of the {@link SteeringBehavior} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     */
    protected SteeringBehavior(Player player, int priority, double weight) {
        this.player = player;
        this.priority = priority;
        this.weight = weight;
    }
    
    /**
     * Gets the current acceleration vector of the behavior.
     * @return The acceleration {@link Vector}.
     */
    public abstract Vector getAccelerationVector();
    
}
