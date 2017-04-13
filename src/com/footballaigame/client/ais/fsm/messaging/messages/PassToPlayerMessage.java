package com.footballaigame.client.ais.fsm.messaging.messages;

import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.Message;

public class PassToPlayerMessage implements Message {
    
    public Player receiver;
    
    public PassToPlayerMessage(Player receiver) {
        this.receiver = receiver;
    }
}
