package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;

public class Wander extends SteeringBehavior {
    
    private Vector wanderTarget;
    
    private Seek seek;
    
    public double wanderRadius;
    
    public double wanderDistance;
    
    public double wanderJitter;
    
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
    
    @Override
    public Vector calculateAccelerationVector() {
        
        // we are working in local space (Player heading = x-coordinate)
        
        Vector diff = new Vector((FsmAI.random.nextDouble() - 0.5), (FsmAI.random.nextDouble() - 0.5), wanderJitter);
        
        wanderTarget = Vector.sum(wanderTarget, diff);
        wanderTarget.resize(wanderRadius);
        wanderTarget = Vector.sum(wanderTarget, new Vector(wanderDistance, 0));
        
        // change to world space
        Vector target = new Vector(wanderTarget.x, wanderTarget.y);
        
        if (player.currentSpeed() > 0.001) {
            Vector m = player.movement.getNormalized();
            
            target.x = wanderTarget.x * m.x - wanderTarget.y * m.y;
            target.y = wanderTarget.x * m.y + wanderTarget.y * m.x;
        }
        
        target = Vector.sum(player.position, target);
        
        seek.target = target;
        
        return seek.calculateAccelerationVector();
    }
}
