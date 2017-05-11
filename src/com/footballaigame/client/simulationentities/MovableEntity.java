package com.footballaigame.client.simulationentities;

import com.footballaigame.client.customdatatypes.Vector;

/**
 * Provides the base class target which all moving game entities are derived.
 */
abstract public class MovableEntity {
    
    /**
     * The movement {@link Vector} of the entity. It describes how entity's position changes in one simulation step.
     */
    public Vector movement;
    
    /**
     * The position {@link Vector} of the entity.
     */
    public Vector position;
    
    /**
     * Initializes a new instance of the {@link MovableEntity} class.
     */
    protected MovableEntity() {
        movement = new Vector();
        position = new Vector();
    }
    
    /**
     * Gets the current speed in meters per simulation step.
     * @return The current speed in meters per simulation step.
     */
    public double getCurrentSpeed() {
        return movement.getLength();
    }
    
    /**
     * Predicts the position in time.
     * @param time The time.
     * @return The predicted position {@link Vector}.
     */
    public Vector predictPositionInTime(double time) {
        return Vector.getSum(position, movement.getMultiplied(time));
    }
}
