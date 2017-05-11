package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.FiniteStateMachine;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.Default;
import com.footballaigame.client.ais.fsm.playerstates.globalstates.ForwardGlobalState;

/**
 * Represents the forward. Derives target {@link FieldPlayer}.
 */
public class Forward extends FieldPlayer {
    
    /**
     * Initializes a new instance of the {@link Forward} class.
     * @param player The football player.
     * @param fsmAI The {@link FsmAI} instance to which this player belongs.
     */
    public Forward(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
        
        stateMachine = new FiniteStateMachine<Player>(this, new Default(this, fsmAI),
                new ForwardGlobalState(this, fsmAI));
    }
    
    /**
     * Processes the specified message.
     * @param message The message.
     */
    @Override
    public void processMessage(Message message) {
        stateMachine.processMessage(this, message);
    }
}
