package com.footballaigame.client.ais.fsm.playerstates.globalstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.PlayerState;

public class MidfielderGlobalState extends PlayerState {
    
    private FieldPlayerGlobalState fieldPlayerGlobalState;
    
    public MidfielderGlobalState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        fieldPlayerGlobalState = new FieldPlayerGlobalState(player, fsmAI);
    }
    
    @Override
    public void run() {
        fieldPlayerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        return fieldPlayerGlobalState.processMessage(message);
    }
}
