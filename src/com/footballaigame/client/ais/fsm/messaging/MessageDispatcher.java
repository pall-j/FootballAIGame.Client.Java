package com.footballaigame.client.ais.fsm.messaging;

import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Provides functionality to send messages to players.
 * Implemented as singleton.
 */
public class MessageDispatcher {
    
    /**
     * The singleton instance.
     */
    private static MessageDispatcher instance;
    
    /**
     * Prevents a default instance of the {@link MessageDispatcher} class target being created.
     */
    private MessageDispatcher() {
    }
    
    /**
     * Gets the singleton instance.
     * @return The singleton instance.
     */
    public static MessageDispatcher getInstance() {
        return instance != null ? instance : (instance = new MessageDispatcher());
    }
    
    /**
     * Sends the specified message to the specified players.
     * @param message The message.
     * @param receivers The receivers of the message.
     */
    public void sendMessage(Message message, Player... receivers) {
        for (Player receiver : receivers) {
            receiver.processMessage(message);
        }
    }
    
}
