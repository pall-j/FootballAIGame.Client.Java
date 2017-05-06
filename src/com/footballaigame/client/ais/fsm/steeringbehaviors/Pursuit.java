package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.simulationentities.MovableEntity;
import com.footballaigame.client.ais.fsm.entities.Player;

public class Pursuit extends SteeringBehavior {
    
    private Arrive targetArrive;
    
    public MovableEntity target;
    
    public Pursuit(Player player, int priority, double weight, MovableEntity target) {
        super(player, priority, weight);
        this.target = target;
        targetArrive = new Arrive(player, priority, weight, target.position);
    }
    
    @Override
    public Vector getAccelerationVector() {
        double distance = Vector.getDistanceBetween(player.position, target.position);
        
        double lookAheadTime = 0;
        if (player.getCurrentSpeed() + target.getCurrentSpeed() > 0)
            lookAheadTime = distance / (player.getCurrentSpeed() + target.getCurrentSpeed());
        
        targetArrive.target = target.getPredictedPositionInTime(lookAheadTime);
        
        return targetArrive.getAccelerationVector();
    }
}
