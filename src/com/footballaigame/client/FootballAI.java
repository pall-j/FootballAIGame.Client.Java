package com.footballaigame.client;

import com.footballaigame.client.simulationentities.GameState;
import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.simulationentities.AIAction;

public interface FootballAI {
    
    /**
     * Called every time the new match simulation with the AI starts.
     * <p>
     * Called before {@link #getParameters()}.
     */
    void initialize();
    
    /**
     * Gets the {@link AIAction} for the specified {@link GameState}.
     *
     * @param state State of the game.
     * @return The {@link AIAction} for the specified {@link GameState}.
     */
    AIAction getAction(GameState state);
    
    /**
     * Gets the player parameters. position and moving vector properties are ignored.
     *
     * @return The array of football players with their parameters set.
     */
    FootballPlayer[] getParameters();
    
}
