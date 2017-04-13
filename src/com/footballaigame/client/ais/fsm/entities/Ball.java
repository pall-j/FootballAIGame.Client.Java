package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballBall;
import com.footballaigame.client.simulationentities.GameState;

public class Ball extends FootballBall {
    
    public Ball(FootballBall ball) {
        position = ball.position;
        movement = ball.movement;
    }
    
    public void loadState(GameState gameState) {
        position.x = gameState.ball.position.x;
        position.y = gameState.ball.position.y;
        movement.x = gameState.ball.movement.x;
        movement.y = gameState.ball.movement.y;
    }
    
}
