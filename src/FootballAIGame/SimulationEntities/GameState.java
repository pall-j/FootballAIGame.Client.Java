package FootballAIGame.SimulationEntities;
        
        import FootballAIGame.CustomDataTypes.Vector;
        import java.nio.ByteBuffer;
        import java.nio.ByteOrder;
        import java.util.Arrays;

/**
 * Represents the state of the football game.
 */
public class GameState {
    
    /**
     * The football players array consisting of 22 players, where first 11
     * players are from the player's team and the rest 11 players are from the opponent's team.
     */
    public FootballPlayer[] footballPlayers;
    
    /**
     * The football ball.
     */
    public Ball ball;
    
    /**
     * The simulation step number specifying to which simulation step this instance belongs.
     */
    public int step;
    
    public boolean kickOff;
    
    /**
     * Parses the specified binary representation of the game state.
     * @param data The binary representation of the game state.
     * @return The parsed game state.
     * @throws IllegalArgumentException Thrown if an error has occurred while parsing the game state.
     */
    public static GameState parse(byte[] data) throws IllegalArgumentException {
        
        float[] floatData = new float[92];
        GameState state = new GameState();
        int step;
        
        if (data.length != floatData.length*4 + 4 + 1)
            throw new IllegalArgumentException("Invalid game state data.");
    
        state.kickOff = data[4] == 1;
    
        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        
        step = byteBuffer.asIntBuffer().get(0); // parse step
        data = Arrays.copyOfRange(data, 5, data.length);
        
        byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asFloatBuffer().get(floatData); // parse vectors
        
        FootballPlayer[] players = new FootballPlayer[22];
        for (int i = 0; i < 22; i++) {
            players[i] = new FootballPlayer();
        }
        
        Ball ball = new Ball();
        ball.position = new Vector(floatData[0], floatData[1]);
        ball.movement = new Vector(floatData[2], floatData[3]);
        
        
        for (int i = 0; i < 22; i++) {
            players[i].position = new Vector(floatData[4 + 4 * i], floatData[4 + 4 * i + 1]);
            players[i].movement = new Vector(floatData[4 + 4 * i + 2], floatData[4 + 4 * i + 2]);
        }
        
        state.ball = ball;
        state.footballPlayers = players;
        state.step = step;
        
        return state;
    }
}
