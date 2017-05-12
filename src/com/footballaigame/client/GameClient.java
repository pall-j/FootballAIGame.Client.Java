package com.footballaigame.client;

import com.footballaigame.client.simulationentities.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * The main AI game client class.
 * <p>
 * Provides methods to start logging in to the game server and listening for the game server commands.
 */
public class GameClient {
    
    /**
     * The time in milliseconds of one simulation step.
     */
    public static final int STEP_INTERVAL = 200;
    
    /**
     * The football field height.
     */
    public static final double FIELD_HEIGHT = 75;
    
    /**
     * The football field width.
     */
    public static final double FIELD_WIDTH = 110;
    
    /**
     * Gets or sets the connection to the server.
     */
    private InetAddress serverAddress;
    
    /**
     * The game server port.
     */
    private int port;
    
    /**
     * The AI instance that will process the game server commands.
     */
    private FootballAI ai;
    
    /**
     * The connection to the server.
     */
    private ServerConnection connection;
    
    /**
     * Initializes a new instance of the {@link GameClient} class.
     *
     * @param serverAddress The game server address.
     * @param port          The game server port.
     * @param ai            The AI.
     */
    public GameClient(InetAddress serverAddress, int port, FootballAI ai) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.ai = ai;
    }
    
    /**
     * Starts this instance. Starts logging in process.
     */
    public void start() {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            
            System.out.println("Enter user name, AI name and access key separated by whitespace.");
            
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            }
            
            if (line == null)
                break;
            
            String[] tokens = line.trim().split(" ");
            
            if (tokens.length != 3) {
                System.out.println("Invalid format!");
                continue;
            }
            
            tryStart(tokens[0], tokens[1], tokens[2]);
        }
        
    }
    
    /**
     * Starts this instance. Starts logging in process. Uses the specified parameters for logging in.
     * @param userName The user's name.
     * @param accessKey The access key.
     */
    public void start(String userName, String accessKey) {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while (true)
        {
            System.out.println("Enter AI name.");
            
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            }
            
            if (line == null)
                break;
            
            String[] tokens = line.trim().split(" ");
            
            if (tokens.length != 1 || tokens[0].length() == 0) {
                System.out.println("Invalid format!");
                continue;
            }
            
            tryStart(userName, tokens[0], accessKey);
        }
    }
    
    /**
     * Tries to start this instance by logging in to server with the specified parameters.
     * @param userName The user name.
     * @param aiName The AI name.
     * @param accessKey The access key.
     */
    public void tryStart(String userName, String aiName, String accessKey) {
        
        connection = ServerConnection.tryConnect(serverAddress, port, userName, aiName, accessKey);
        
        if (connection == null)
            return;
        
        System.out.println("Connected.");
        startProcessing();
    }
    
    /**
     * Starts listening for game commands and processing them.
     */
    private void startProcessing() {
        
        while (true) {
            
            try {
                
                Command command = connection.receiveCommand();
                process(command);
                
            } catch (IOException e) {
                
                System.out.println("Game Server has stopped responding.");
                start();
                
            }
        }
    }
    
    /**
     * Processes the specified command. Calls appropriate {@link FootballAI} methods.
     *
     * @param command The command to be processed.
     */
    private void process(Command command) {
        switch (command.type) {
            case GET_PARAMETERS:
                ai.initialize();
                try {
                    connection.sendParameters(ai.getParameters());
                } catch (IOException e) {
                    System.out.println("Error while sending parameters to the server.");
                }
                
                break;
            case GET_ACTION:
                try {
                    GameState state = GameState.parse(command.data);
                    connection.send(ai.getAction(state));
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException e) {
                    System.out.println("Error while sending action to the server.");
                }
                break;
        }
    }
    
}
