package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

public class Flee extends SteeringBehavior {
    
    public Vector from;
    
    public double safeDistance;
    
    public Flee(Player player, int priority, double weight, Vector from, double safeDistance) {
        super(player, priority, weight);
        this.from = from;
        this.safeDistance = safeDistance;
    }
    
    @Override
    public Vector calculateAccelerationVector() {
        
        if (Vector.distanceBetween(player.position, from) >= safeDistance)
            return new Vector(0, 0);
        
        Vector desiredMovement = Vector.difference(player.movement, from);
        
        if (Math.abs(desiredMovement.lengthSquared()) < 0.01)
            desiredMovement = new Vector(1, 0);
        
        desiredMovement.resize(player.maxSpeed());
        
        Vector acceleration = Vector.difference(desiredMovement, player.movement);
        acceleration.truncate(player.maxAcceleration());
        
        return acceleration;
    }
}
