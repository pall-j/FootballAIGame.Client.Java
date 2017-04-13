package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

public class Seek extends SteeringBehavior {
    
    public Vector target;
    
    public Seek(Player player, int priority, double weight, Vector target) {
        super(player, priority, weight);
        this.target = target;
    }
    
    @Override
    public Vector calculateAccelerationVector() {
        Vector acceleration = new Vector(0, 0);
        
        if (target == null) return acceleration;
        
        Vector desiredMovement = Vector.difference(target, player.position);
        desiredMovement.truncate(player.maxSpeed());
        
        acceleration = Vector.difference(desiredMovement, player.movement);
        acceleration.truncate(player.maxAcceleration());
        
        return acceleration;
    }
}
