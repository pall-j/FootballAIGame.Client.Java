package FootballAIGame;

import FootballAIGame.SimulationEntities.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class GameClient {
    
    public static final int stepInterval = 200; // [ms]
    
    private InetAddress serverAdress;
    private int port;
    private FootballAI ai;
    private ServerConnection connection;
    
    
    public GameClient(InetAddress serverAddress, int port, FootballAI ai) {
        this.serverAdress = serverAddress;
        this.port = port;
        this.ai = ai;
    }
    
    public void start() {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            
            while (true) {
                
                System.out.println("Enter user name and AI name separated by whitespace.");
                String line = reader.readLine();
                String[] tokens = line.split(" ");
                
                if (tokens.length != 2) {
                    System.out.println("Invalid format!");
                    continue;
                }
                
                try {
                    
                    connection = ServerConnection.connect(serverAdress, port, tokens[0], tokens[1]);
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
