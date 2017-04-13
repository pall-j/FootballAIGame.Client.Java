package com.footballaigame.client.simulationentities;

import com.footballaigame.client.customdatatypes.Vector;

public class MovableEntity {
    
    /**
     * The movement vector of the entity. It describes how entity's position changes in one simulation step.
     */
    public Vector movement;
    
    /**
     * The position of the entity.
     */
    public Vector position;
    
    public double currentSpeed() {
        return movement.length();
    }
    
    public Vector predictedPositionInTime(double time) {
        return Vector.sum(position, movement.getMultiplied(time));
    }
}
