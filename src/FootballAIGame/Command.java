package FootballAIGame;

public class Command {
    
    public CommandType type;
    
    public byte[] data;
    
    public enum CommandType
    {
        GET_PARAMETERS, GET_ACTION
    }
}
