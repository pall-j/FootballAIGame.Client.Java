package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.FiniteStateMachine;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.Default;
import com.footballaigame.client.ais.fsm.playerstates.globalstates.ForwardGlobalState;

public class Forward extends FieldPlayer {
    
    public Forward(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
        
        stateMachine = new FiniteStateMachine<Player>(this, new Default(this, fsmAI),
                new ForwardGlobalState(this, fsmAI));
    }
    
    @Override
    public void processMessage(Message message) {
        stateMachine.processMessage(this, message);
    }
}
