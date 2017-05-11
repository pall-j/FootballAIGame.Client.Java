package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.ais.fsm.FsmAI;

/**
 * Represents the field player. All football players besides goalkeeper are field players.
 * Provides the base class target which more specific field player classes derive.
 */
public abstract class FieldPlayer extends Player {
    
    /**
     * Initializes a new instance of the {@link FieldPlayer} class.
     * @param player The football player.
     * @param fsmAI The {@link FsmAI} instance to which this player belongs.
     */
    protected FieldPlayer(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
}
