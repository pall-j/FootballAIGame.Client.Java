package FootballAIGame.AI.FSM.UserClasses;

import FootballAIGame.AI.FSM.FootballAI;
import FootballAIGame.AI.FSM.SimulationEntities.FootballPlayer;
import FootballAIGame.AI.FSM.SimulationEntities.GameAction;
import FootballAIGame.AI.FSM.SimulationEntities.GameState;
import FootballAIGame.AI.FSM.UserClasses.Entities.Ball;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Utilities.SupportPositionsManager;

import java.util.Random;

/**
 * The main Ai class where the Ai behavior is defined.
 */
public class Ai implements FootballAI {
    
    public static Random random;
    
    public Ball ball;
    
    public Team myTeam;
    
    public Team opponentTeam;
    
    public GameState currentState;
    
    private static Ai instance;
    
    public static Ai getInstance() {
        return instance != null ? instance : (instance = new Ai());
    }
    
    private Ai() {
        
    }
    
    @Override
    public void initialize() {
        if (random == null)
            random = new Random();
    }
    
    @Override
    public GameAction getAction(GameState gameState) {
        
        if (gameState.step == 0 || myTeam == null) {
            ball = new Ball(gameState.ball);
            myTeam = new Team(getParameters());
            opponentTeam = new Team(getParameters()); // expect opponent to have the same parameters
        }
        
        // AI entities (wrappers of SimulationEntities) are set accordingly
        currentState = gameState;
        ball.loadState(gameState);
        opponentTeam.loadState(gameState, false); // must be loaded before my team!
        myTeam.loadState(gameState, true);
        SupportPositionsManager.getInstance().update();
        
        // new action
        GameAction currentAction = new GameAction();
        currentAction.step = gameState.step;
        currentAction.playerActions = myTeam.getActions();
        
        return currentAction;
    }
    
    @Override
    public FootballPlayer[] getParameters() {
        
        FootballPlayer[] players = new FootballPlayer[11];
        
        for (int i = 0; i < 11; i++) {
            players[i] = new FootballPlayer(i);
            players[i].speed = 0.4f;
            players[i].kickPower = 0.2f;
            players[i].possession = 0.2f;
            players[i].precision = 0.2f;
        }
        
        return players;
    }
}
