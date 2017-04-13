package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.FiniteStateMachine;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.Default;
import com.footballaigame.client.ais.fsm.playerstates.globalstates.GoalKeeperGlobalState;

public class GoalKeeper extends Player {
    
    public GoalKeeper(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
        
        stateMachine = new FiniteStateMachine<Player>(this, new Default(this, fsmAI),
                new GoalKeeperGlobalState(this, fsmAI));
    }
    
    @Override
    public void processMessage(Message message) {
        stateMachine.processMessage(this, message);
    }
}
