package FootballAIGame.AI.FSM;

import FootballAIGame.AI.FSM.UserClasses.FsmAI;

import java.net.*;


/**
 * The class that contains the entry point of the application.
 */
public class Application {
    
    /**
     * The entry point of the application. Creates and starts the {@link GameClient}.
     */
    public static void main(String[] args) {
    
        try {
        
            GameClient client = new GameClient(InetAddress.getByName("gameserver.northeurope.cloudapp.azure.com"), 50030, new FsmAI());
            //GameClient client = new GameClient(InetAddress.getLocalHost(), 50030, new FsmAI());
        
            client.start();
            //client.start("Serillan", null); // fixed user with his access key (suitable for local simulators)
        
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }
    
}
