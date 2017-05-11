package com.footballaigame.client;

import com.footballaigame.client.ais.basic.BasicAI;
import com.footballaigame.client.ais.fsm.FsmAI;


import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * The class that contains the entry point of the application.
 */
public class Application {
    
    /**
     * The entry point of the application. Creates and starts the {@link GameClient}.
     */
    public static void main(String[] args) {
    
        try {
    
            // ------------------------ FSM AI ----
            //GameClient client = new GameClient(InetAddress.getByName("gameserver.footballaigame.com"), 50030, new FsmAI());
            GameClient client = new GameClient(InetAddress.getLocalHost(), 50030, new FsmAI());
    
            // ----------------------- BASIC (RANDOM) AI ----
            //GameClient client = new GameClient(InetAddress.getByName("gameserver.footballaigame.com"), 50030, new BasicAI());
            //GameClient client = new GameClient(InetAddress.getLocalHost(), 50030, new BasicAI());
        
            
            //client.start();
            client.start("UserName", null); // fixed user with his access key (suitable for connecting to local simulators)
        
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }
    
}
