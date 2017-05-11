package com.footballaigame.client.ais.fsm.playerstates.globalstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.PlayerState;

/**
 * Represents the defender's global state. Keeps {@link FieldPlayerGlobalState} internally and calls
 * its methods at the end of its own methods.
 */
public class DefenderGlobalState extends PlayerState {
    
    /**
     * The field player's global state that is used at the end of the this state methods.
     */
    private FieldPlayerGlobalState fieldPlayerGlobalState;
    
    /**
     * Initializes a new instance of the {@link DefenderGlobalState} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public DefenderGlobalState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        fieldPlayerGlobalState = new FieldPlayerGlobalState(player, fsmAI);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
    @Override
    public void run() {
        fieldPlayerGlobalState.run();
    }
    
    /**
     * Processes the specified message.
     * @param message The message.
     * @return True if the specified message was handled; otherwise, false.
     */
    @Override
    public boolean processMessage(Message message) {
        return fieldPlayerGlobalState.processMessage(message);
    }
}
