package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.entities.Player;

public abstract class SteeringBehavior {
    
    protected Player player;
    
    public int priority;
    
    public double weight;
    
    public abstract Vector getAccelerationVector();
    
    public SteeringBehavior(Player player, int priority, double weight) {
        this.player = player;
        this.priority = priority;
        this.weight = weight;
    }
}
