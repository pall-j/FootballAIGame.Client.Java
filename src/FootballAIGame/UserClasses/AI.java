package FootballAIGame.UserClasses;
import FootballAIGame.FootballAI;
import FootballAIGame.GameClient;
import FootballAIGame.SimulationEntities.FootballPlayer;
import FootballAIGame.SimulationEntities.GameAction;
import FootballAIGame.SimulationEntities.GameState;
import FootballAIGame.SimulationEntities.PlayerAction;

import java.util.Random;

public class AI implements FootballAI {
    
    private static Random random;
    
    private boolean isOnLeft;
    
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
    
    
            // kick correction (to maximum allowed)
            double maxKickLength = (15 + players[i].kickPower * 10) * GameClient.stepInterval / 1000;
            double currentKickSpeed = playerAction.kick.length();
            playerAction.kick.x *= maxKickLength / currentKickSpeed;
            playerAction.kick.y *= maxKickLength / currentKickSpeed;
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
