package FootballAIGame;

/**
 * Represents the command received from the game server.
 */
public class Command {
    
    /**
     * The command type.
     */
    public CommandType type;
    
    /**
     * The command data.
     */
    public byte[] data;
    
    public enum CommandType
    {
        GET_PARAMETERS, GET_ACTION
    }
}
