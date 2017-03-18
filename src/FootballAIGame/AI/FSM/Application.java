package FootballAIGame.AI.FSM;

import FootballAIGame.AI.FSM.UserClasses.Ai;

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
            
            GameClient client = new GameClient(InetAddress.getByName("gameserver.northeurope.cloudapp.azure.com"), 50030, new Ai());
            //GameClient client = new GameClient(InetAddress.getLocalHost(), 50030, new Ai());
            client.start();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }
    
}
