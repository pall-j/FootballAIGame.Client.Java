package com.footballaigame.client.ais.fsm.steeringbehaviors;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.simulationentities.FootballBall;
import com.footballaigame.client.simulationentities.MovableEntity;
import com.footballaigame.client.ais.fsm.entities.Ball;
import com.footballaigame.client.ais.fsm.entities.Player;

public class Interpose extends SteeringBehavior {
    
    public MovableEntity first;
    
    public MovableEntity second;
    
    private Arrive arrive;
    
    public double preferredDistanceFromSecond;
    
    public Interpose(Player player, int priority, double weight, MovableEntity first, MovableEntity second) {
        super(player, priority, weight);
        this.first = first;
        this.second = second;
        arrive = new Arrive(player, priority, weight, player.position);
        preferredDistanceFromSecond = Vector.distanceBetween(second.position, first.position) / 2.0;
    }
    
    public Interpose(Player player, int priority, double weight, MovableEntity first, Vector secondPosition) {
        super(player, priority, weight);
        
        this.first = first;
        this.second = new Ball(new FootballBall()); // artificial movable entity for representing second
        this.second.position = secondPosition;
        
        arrive = new Arrive(player, priority, weight, player.position);
        preferredDistanceFromSecond = Vector.distanceBetween(second.position, first.position) / 2.0;
    }
    
    @Override
    public Vector calculateAccelerationVector() {
        
        Vector firstToSecond = new Vector(first.position, second.position);
        Vector firstToPlayer = new Vector(first.position, player.position);
        
        double firstToTargetDistance = Vector.dotProduct(firstToPlayer, firstToSecond) / firstToSecond.length();
        
        if (firstToTargetDistance < 0 || firstToTargetDistance > firstToSecond.length()) {
            arrive.target = Vector.sum(first.position, firstToSecond.getMultiplied(1 / 2.0)); // go to midpoint
            return arrive.calculateAccelerationVector();
        }
        
        arrive.target = Vector.sum(first.position, firstToSecond.getResized(firstToTargetDistance));
        
        double playerToTargetDistance = Vector.distanceBetween(arrive.target, player.position);
        
        if (playerToTargetDistance < 0.01 && firstToSecond.length() > preferredDistanceFromSecond) {
            // move player to meet DistanceFromSecond condition
            arrive.target = Vector.sum(first.position, firstToSecond.getResized(firstToSecond.length() - preferredDistanceFromSecond));
        }
        
        
        return arrive.calculateAccelerationVector();
    }
}