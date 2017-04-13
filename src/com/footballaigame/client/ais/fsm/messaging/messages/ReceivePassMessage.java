package com.footballaigame.client.ais.fsm.messaging.messages;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.messaging.Message;

public class ReceivePassMessage implements Message {
    
    public Vector PassTarget;
    
    public ReceivePassMessage(Vector target) {
        PassTarget = target;
    }
    
}
