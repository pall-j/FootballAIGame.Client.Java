package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.ais.fsm.FsmAI;

public abstract class FieldPlayer extends Player {
    
    protected FieldPlayer(FootballPlayer player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
}
