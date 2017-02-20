package FootballAIGame.UserClasses;
import FootballAIGame.CustomDataTypes.Vector;
import FootballAIGame.FootballAI;
import FootballAIGame.SimulationEntities.FootballPlayer;
import FootballAIGame.SimulationEntities.GameAction;
import FootballAIGame.SimulationEntities.GameState;
import FootballAIGame.SimulationEntities.PlayerAction;

import java.util.Random;

/**
 * The main AI class where the AI behavior is defined.
 */
public class AI implements FootballAI {
    
    private static Random random;
    
    /**
     * The value indicating whether the AI football team holds currently the left goal post.
     */
    private boolean isOnLeft;
    
    /**
     * Gets or sets the football players with their parameters set. <p>
     * Set after GetParameters is called. Used to know players parameters at every {@link #getAction(GameState)} call.
     */
    private FootballPlayer[] players;
    
    @Override
    public void initialize() {
        if (random == null)
            random = new Random();
    }
    
    @Override
    public GameAction getAction(GameState gameState) {
        
        if (gameState.step == 0)
            isOnLeft = gameState.footballPlayers[0].position.x < 55;
        
        if (gameState.step == 750)
            isOnLeft = !isOnLeft;
        
        GameAction action = new GameAction();
        action.playerActions = new PlayerAction[11];
        action.step = gameState.step;
    
        for (int i = 0; i < 11; i++) {
            
            FootballPlayer player = gameState.footballPlayers[i];
            PlayerAction playerAction = new PlayerAction();
            action.playerActions[i] = playerAction;
    
            playerAction.movement.x = random.nextDouble() - 0.5;
            playerAction.movement.y = random.nextDouble() - 0.5;
    
    
            if ((player.position.x > 110 && playerAction.movement.x > 0) || 
                    (player.position.x <= 110.01 && player.position.x + playerAction.movement.x > 110))
                playerAction.movement.x *= -1;
    
            if ((player.position.y > 75 && playerAction.movement.y > 0) || 
                    (player.position.y < 75.01 && player.position.y + playerAction.movement.y > 75))
                playerAction.movement.y *= -1;
    
            if ((player.position.x < 0 && playerAction.movement.x < 0) || 
                    (player.position.x >= 0 && player.position.x + playerAction.movement.x < 0))
                playerAction.movement.x *= -1;
    
            if ((player.position.y < 0 && playerAction.movement.y < 0) || 
                    (player.position.y >= 0 && player.position.y + playerAction.movement.y < 0))
                playerAction.movement.y *= -1;
    
            if (isOnLeft)
            {
                playerAction.kick.x = 110 - player.position.x;
                playerAction.kick.y = (75 / 2f) - player.position.y;
            }
            else
            {
                playerAction.kick.x = -player.position.x;
                playerAction.kick.y = (75 / 2f) - player.position.y;
            }
    
    
            // acceleration correction
            Vector toNewMovement = Vector.difference(playerAction.movement, player.movement);
            if (toNewMovement.length() > player.maxAcceleration())
            {
                toNewMovement.resize(player.maxAcceleration());
                playerAction.movement = Vector.sum(player.movement, toNewMovement);
            }
    
            // speed correction
            if (playerAction.movement.length() > player.maxSpeed())
                playerAction.movement.resize(player.maxSpeed());
    
            // kick correction
            if (playerAction.kick.length() > player.maxKickSpeed())
                playerAction.kick.resize(player.maxKickSpeed());
        
        }
        
        return action;
    }
    
    @Override
    public FootballPlayer[] getParameters() {
        if (players != null) return players;
    
        players = new FootballPlayer[11];
        for (int i = 0; i < 11; i++)
        {
            players[i] = new FootballPlayer();
            players[i].speed = 0.4f;
            players[i].kickPower = 0.2f;
            players[i].possession = 0.2f;
            players[i].precision = 0.2f;
        }
    
        return players;
    }
}
