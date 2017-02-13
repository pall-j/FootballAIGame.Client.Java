package FootballAIGame;

import com.sun.deploy.util.ArrayUtil;

import java.io.*;
import java.net.*;
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
                Command result = new Command();
                // todo
                return result;
            }
            else if (firstLine.length() >= 10 && firstLine.substring(firstLine.length() - 10).equals("GET ACTION")) {
                Command result = new Command();
                // todo
                return result;
            }
            else if (firstLine != "keepalive") {
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
    }

}
