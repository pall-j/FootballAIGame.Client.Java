package FootballAIGame;

import FootballAIGame.SimulationEntities.FootballPlayer;
import FootballAIGame.SimulationEntities.GameAction;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ServerConnection {

    private Socket server;
    private InputStream inputStream;
    private OutputStream outputStream;

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

    public String receiveMessage() throws  IOException {
        return readLine();
    }
    
    public Command receiveCommand() throws IOException {
       
        while (true) {
            
            String firstLine = readLine();
            if (firstLine.length() >= 14 && firstLine.substring(firstLine.length() - 14).equals("GET PARAMETERS")) {
               
                Command command = new Command();
                command.type = Command.CommandType.GET_PARAMETERS;
                return command;
            }
            else if (firstLine.length() >= 10 && firstLine.substring(firstLine.length() - 10).equals("GET ACTION")) {
               
                Command command = new Command();
                command.type = Command.CommandType.GET_ACTION;
                command.data = new byte[372];
                inputStream.read(command.data, 0, command.data.length);
                
                return command;
            }
            else if (!firstLine.equals("keepalive")) {
                System.out.println(firstLine);
            }
        }
    }
    
    public String readLine() throws IOException {
        List<Byte> bytes = new ArrayList<>();

        while (true) {
            Byte next = (byte)inputStream.read();

            if (next == (byte)('\n'))
                break;
            bytes.add(next);
        }

        byte[] bytesArr = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bytesArr[i] = bytes.get(i);
        }
        
        return new String(bytesArr, "UTF-8");
    }

    public void send(String message) throws IOException {
        outputStream.write((message + "\n").getBytes());
    }

    public void send(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }
    
    public void send(GameAction action) throws IOException {
        
        float[] data = new float[44];
    
        for (int i = 0; i < 11; i++) {
            data[4*i] = (float)action.playerActions[i].movement.x;
            data[4*i + 1] = (float)action.playerActions[i].movement.y;
            data[4*i + 2] = (float)action.playerActions[i].kick.x;
            data[4*i + 3] = (float)action.playerActions[i].kick.y;
        }
        
        ByteBuffer buffer = ByteBuffer.allocate(data.length * 4 + 4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(action.step);
        for (float datum : data) {
            buffer.putFloat(datum);
        }
        
        send("ACTION");
        send(buffer.array());
        
    }
    
    public void sendParameters(FootballPlayer[] players) throws IOException {
        
        float[] data = new float[44];
        for (int i = 0; i < 11; i++) {
            data[4*i] = players[i].speed;
            data[4*i + 1] = players[i].precision;
            data[4*i + 2] = players[i].possession;
            data[4*i + 3] = players[i].kickPower;
        }
    
        ByteBuffer buffer = ByteBuffer.allocate(data.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        for (float datum : data) {
            buffer.putFloat(datum);
        }
    
        send("PARAMETERS");
        send(buffer.array());
    
    
    }
}
