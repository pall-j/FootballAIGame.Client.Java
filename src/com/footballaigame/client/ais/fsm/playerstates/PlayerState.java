package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.State;

public abstract class PlayerState extends State<Player> {
    
    protected Player player;
    
    public PlayerState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        this.player = player;
    }
}
