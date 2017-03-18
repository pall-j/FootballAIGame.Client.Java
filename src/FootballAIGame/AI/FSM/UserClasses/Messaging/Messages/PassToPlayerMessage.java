package FootballAIGame.AI.FSM.UserClasses.Messaging.Messages;

import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;

public class PassToPlayerMessage implements Message {
    
    public Player receiver;
    
    public PassToPlayerMessage(Player receiver) {
        this.receiver = receiver;
    }
}
