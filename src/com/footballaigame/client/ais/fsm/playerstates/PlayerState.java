package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.State;

/**
 * Provides the base class from which the classes that represent player's states are derived.
 * Contains the shared functionality of the player's states.
 */
public abstract class PlayerState extends State<Player> {
    
    /**
     * The player to whom this instance belongs.
     */
    protected Player player;
    
    /**
     * Initializes a new instance of the {@link PlayerState} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    protected PlayerState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        this.player = player;
    }
}
