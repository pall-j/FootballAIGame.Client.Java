package FootballAIGame;
import FootballAIGame.UserClasses.AI;

import java.net.*;


public class Application {

    public static void main(String[] args) {
    
        try {
            
            GameClient client = new GameClient(InetAddress.getByName("gameserver.northeurope.cloudapp.azure.com"),
                    50030, new AI());
            client.start();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    
    }

}
