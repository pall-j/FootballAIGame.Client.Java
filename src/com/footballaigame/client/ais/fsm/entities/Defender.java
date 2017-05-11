package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.FiniteStateMachine;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.Default;
import com.footballaigame.client.ais.fsm.playerstates.globalstates.DefenderGlobalState;

/**
 * Represents the defender. Derives target {@link FieldPlayer}.
 */
public class Defender extends FieldPlayer {
    
    /**
     * Initializes a new instance of the {@link Defender} class.
     * @param player The football player.
     * @param fsmAI The {@link FsmAI} instance to which this player belongs.
     */
    public Defender(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
        
        stateMachine = new FiniteStateMachine<Player>(this, new Default(this, fsmAI),
                new DefenderGlobalState(this, fsmAI));
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
