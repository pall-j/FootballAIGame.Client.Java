package FootballAIGame.AI.FSM;

import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.SimulationEntities.GameAction;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for keeping TCP connection to the game server.
 * Provides methods for communicating with the server.
 */
public class ServerConnection {
    
    /**
     * The socket associated with the game server.
     */
    private Socket server;
    
    /**
     * The input stream associated with the game server.
     */
    private InputStream inputStream;
    
    /**
     * The output stream associated with the game server.
     */
    private OutputStream outputStream;
    
    /**
     * Connects to the game server.
     *
     * @param address  The game server IP address.
     * @param port     The game server port.
     * @param userName Name of the user.
     * @param aiName   Desired name of the Ai.
     * @return The server connection.
     * @throws IOException Thrown if an error has occurred while connecting to the server.
     */
    public static ServerConnection connect(InetAddress address, int port, String userName, String aiName) throws IOException {
        ServerConnection connection = new ServerConnection();
        try {
            
            connection.server = new Socket();
            connection.server.connect(new InetSocketAddress(address, port), 5000);
            connection.inputStream = connection.server.getInputStream();
            connection.outputStream = connection.server.getOutputStream();
            
            connection.send("LOGIN " + userName + " " + aiName);
            
            String message = connection.receiveMessage();
            
            if (message.equals("CONNECTED"))
                return connection;
            else
                throw new IOException();
            
        } catch (IOException e) {
            throw new IOException("Server is not responding.");
        }
        
    }
    
    /**
     * Receives the string message from the server.
     *
     * @return The string message.
     * @throws IOException Thrown if an error has occurred while receiving a message.
     */
    public String receiveMessage() throws IOException {
        return readLine();
    }
    
    /**
     * Receives the {@link Command} from the server.
     *
     * @return The server's {@link Command}.
     * @throws IOException Thrown if an error has occurred while receiving a command.
     */
    public Command receiveCommand() throws IOException {
        
        while (true) {
            
            String firstLine = readLine();
            if (firstLine.length() >= 14 && firstLine.substring(firstLine.length() - 14).equals("GET PARAMETERS")) {
                
                Command command = new Command();
                command.type = Command.CommandType.GET_PARAMETERS;
                return command;
            } else if (firstLine.length() >= 10 && firstLine.substring(firstLine.length() - 10).equals("GET ACTION")) {
                
                Command command = new Command();
                command.type = Command.CommandType.GET_ACTION;
                command.data = new byte[373];
                inputStream.read(command.data, 0, command.data.length);
                
                return command;
            } else if (!firstLine.equals("keepalive")) {
                System.out.println(firstLine);
            }
        }
    }
    
    /**
     * Reads the next line received from the server.
     *
     * @return The next line received from the server.
     * @throws IOException Thrown if an error has occurred while reading a line.
     */
    private String readLine() throws IOException {
        List<Byte> bytes = new ArrayList<Byte>();
        
        while (true) {
            Byte next = (byte) inputStream.read();
            
            if (next == (byte) ('\n'))
                break;
            bytes.add(next);
        }
        
        byte[] bytesArr = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bytesArr[i] = bytes.get(i);
        }
        
        return new String(bytesArr, "UTF-8");
    }
    
    /**
     * Sends the specified {@link GameAction} to the game server.
     *
     * @param action The action to be sent.
     * @throws IOException Thrown if an error has occurred while sending the action.
     */
    public void send(GameAction action) throws IOException {
        
        float[] data = new float[44];
        
        for (int i = 0; i < 11; i++) {
            data[4 * i] = (float) action.playerActions[i].movement.x;
            data[4 * i + 1] = (float) action.playerActions[i].movement.y;
            data[4 * i + 2] = (float) action.playerActions[i].kick.x;
            data[4 * i + 3] = (float) action.playerActions[i].kick.y;
        }
        
        ByteBuffer buffer = ByteBuffer.allocate(data.length * 4 + 4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(action.step);
        for (float datum : data) {
            buffer.putFloat(datum);
        }
        
        send("ACTION");
        send(buffer.array());
        
    }
    
    /**
     * Sends the players parameters to the game server.
     *
     * @param players The players with their parameters set to be sent.
     * @throws IOException Thrown if an error has occurred while sending the players parameters.
     */
    public void sendParameters(FootballPlayer[] players) throws IOException {
        
        float[] data = new float[44];
        for (int i = 0; i < 11; i++) {
            data[4 * i] = players[i].speed;
            data[4 * i + 1] = players[i].precision;
            data[4 * i + 2] = players[i].possession;
            data[4 * i + 3] = players[i].kickPower;
        }
        
        ByteBuffer buffer = ByteBuffer.allocate(data.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        for (float datum : data) {
            buffer.putFloat(datum);
        }
        
        send("PARAMETERS");
        send(buffer.array());
        
        
    }
    
    /**
     * Sends the specified String message to the game server.
     *
     * @param message The message to be sent.
     * @throws IOException Thrown if an error has occurred while sending the message.
     */
    public void send(String message) throws IOException {
        outputStream.write((message + "\n").getBytes());
    }
    
    /**
     * Sends the specified data to the game server.
     *
     * @param data The data to be sent.
     * @throws IOException Thrown if an error has occurred while sending the data.
     */
    public void send(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }
    
}
