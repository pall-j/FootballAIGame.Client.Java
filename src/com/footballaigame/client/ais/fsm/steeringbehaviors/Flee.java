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
    public Vector getAccelerationVector() {
        
        if (Vector.getDistanceBetween(player.position, from) >= safeDistance)
            return new Vector(0, 0);
        
        Vector desiredMovement = Vector.getDifference(player.movement, from);
        
        if (Math.abs(desiredMovement.getLengthSquared()) < 0.01)
            desiredMovement = new Vector(1, 0);
        
        desiredMovement.resize(player.getMaxSpeed());
        
        Vector acceleration = Vector.getDifference(desiredMovement, player.movement);
        acceleration.truncate(player.getMaxAcceleration());
        
        return acceleration;
    }
}
