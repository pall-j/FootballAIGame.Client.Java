package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Represents the behavior where the player is wandering around the specified position.
 */
public class Wander extends SteeringBehavior {
    
    /**
     * The wander target.
     */
    private Vector wanderTarget;
    
    /**
     * The {@link Seek} to the wander target.
     */
    private Seek seek;
    
    /**
     * The wander radius.
     */
    public double wanderRadius;
    
    /**
     * The wander distance.
     */
    public double wanderDistance;
    
    /**
     * The wander jitter.
     */
    public double wanderJitter;
    
    /**
     * Initializes a new instance of the {@link Wander} class.
     * @param player The player.
     * @param priority The priority.
     * @param weight The weight.
     * @param wanderDistance The wander distance.
     * @param wanderRadius The wander radius.
     * @param wanderJitter The wander jitter.
     */
    public Wander(Player player, int priority, double weight, double wanderDistance, double wanderRadius,
                  double wanderJitter) {
        
        super(player, priority, weight);
        
        this.wanderDistance = wanderDistance;
        this.wanderRadius = wanderRadius;
        this.wanderJitter = wanderJitter;
        
        // initial wander target (in local space)
        wanderTarget = new Vector(this.wanderDistance + this.wanderRadius, 0);
        seek = new Seek(player, priority, weight, player.position);
    }
    
    /**
     * Gets the current acceleration vector of the behavior.
     * @return The acceleration {@link Vector}.
     */
    @Override
    public Vector getAccelerationVector() {
        
        // we are working in local space (Player heading = x-coordinate)
        
        Vector diff = new Vector((FsmAI.random.nextDouble() - 0.5), (FsmAI.random.nextDouble() - 0.5), wanderJitter);
        
        wanderTarget = Vector.getSum(wanderTarget, diff);
        wanderTarget.resize(wanderRadius);
        wanderTarget = Vector.getSum(wanderTarget, new Vector(wanderDistance, 0));
        
        // change to world space
        Vector target = new Vector(wanderTarget.x, wanderTarget.y);
        
        if (player.getCurrentSpeed() > 0.001) {
            Vector m = player.movement.getNormalized();
            
            target.x = wanderTarget.x * m.x - wanderTarget.y * m.y;
            target.y = wanderTarget.x * m.y + wanderTarget.y * m.x;
        }
        
        target = Vector.getSum(player.position, target);
        
        seek.target = target;
        
        return seek.getAccelerationVector();
    }
}
