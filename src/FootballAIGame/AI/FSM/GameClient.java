package FootballAIGame.AI.FSM;

import FootballAIGame.AI.FSM.SimulationEntities.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * The main FsmAI game client class.
 * <p>
 * Provides methods to start logging in to the game server and listening for the game server commands.
 */
public class GameClient {
    
    /**
     * The time in milliseconds of one simulation step.
     */
    public static final int STEP_INTERVAL = 200; // [ms]
    
    /**
     * The football field height in meters.
     */
    public static final double FIELD_HEIGHT = 75; // [m]
    
    /**
     * The football field width in meters.
     */
    public static final double FIELD_WIDTH = 110; // [m]
    
    /**
     * Gets or sets the connection to the server.
     */
    private InetAddress serverAddress;
    
    /**
     * The game server port.
     */
    private int port;
    
    /**
     * The FsmAI instance that will process the game server commands.
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
     * @param ai            The FsmAI.
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
        
        try {
            
            while (true) {
                
                System.out.println("Enter user name and FsmAI name separated by whitespace.");
                String line = reader.readLine();
                String[] tokens = line.split(" ");
                
                if (tokens.length != 2) {
                    System.out.println("Invalid format!");
                    continue;
                }
                
                try {
                    
                    connection = ServerConnection.connect(serverAddress, port, tokens[0], tokens[1]);
                    System.out.println("Connected.");
                    startProcessing();
                    break;
                    
                    
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                
            }
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        
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
