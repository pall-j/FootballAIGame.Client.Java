package com.footballaigame.client.ais.fsm.messaging.messages;

import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.Message;

/**
 * Represents the message that tells player to pass the ball to the specified receiver.
 */
public class PassToPlayerMessage implements Message {
    
    /**
     * The pass receiver.
     */
    public Player receiver;
    
    /**
     * Initializes a new instance of the {@link PassToPlayerMessage} class.
     * @param receiver The pass receiver.
     */
    public PassToPlayerMessage(Player receiver) {
        this.receiver = receiver;
    }
}
