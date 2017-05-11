package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballBall;
import com.footballaigame.client.simulationentities.GameState;

/**
 * Extends the {@link FootballBall}. Adds method for loading game state.
 */
public class Ball extends FootballBall {
    
    /**
     * Initializes a new instance of the {@link Ball} class.
     * @param ball The football ball.
     */
    public Ball(FootballBall ball) {
        position = ball.position;
        movement = ball.movement;
    }
    
    /**
     * Loads the state. Updates position and movement vector accordingly.
     * @param gameState The state of the game.
     */
    public void loadState(GameState gameState) {
        position.x = gameState.ball.position.x;
        position.y = gameState.ball.position.y;
        movement.x = gameState.ball.movement.x;
        movement.y = gameState.ball.movement.y;
    }
    
}
