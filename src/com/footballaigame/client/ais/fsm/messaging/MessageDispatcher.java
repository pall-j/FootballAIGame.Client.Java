package com.footballaigame.client.ais.fsm.messaging;

import com.footballaigame.client.ais.fsm.entities.Player;

public class MessageDispatcher {
    
    private static MessageDispatcher instance;
    
    private MessageDispatcher() {
    }
    
    public static MessageDispatcher getInstance() {
        return instance != null ? instance : (instance = new MessageDispatcher());
    }
    
    public void sendMessage(Message message, Player... receivers) {
        for (Player receiver : receivers) {
            receiver.processMessage(message);
        }
    }
    
}
