package FootballAIGame.AI.FSM.UserClasses.Messaging.Messages;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;

public class ReceivePassMessage extends Message {
    
    public Vector PassTarget;
    
    public ReceivePassMessage(Vector target) {
        PassTarget = target;
    }
    
}
