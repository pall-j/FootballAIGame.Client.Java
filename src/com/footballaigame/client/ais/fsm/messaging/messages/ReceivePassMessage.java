package com.footballaigame.client.ais.fsm.messaging.messages;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.messaging.Message;

/**
 * Represents the message that tells player to go to the specified position and wait there for a pass.
 */
public class ReceivePassMessage implements Message {
    
    /**
     * The pass target.
     */
    public Vector PassTarget;
    
    /**
     * Initializes a new instance of the {@link ReceivePassMessage} class.
     * @param target
     */
    public ReceivePassMessage(Vector target) {
        PassTarget = target;
    }
    
}
