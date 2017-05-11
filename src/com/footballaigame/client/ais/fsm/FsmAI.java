package com.footballaigame.client.ais.fsm;

import com.footballaigame.client.FootballAI;
import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.simulationentities.AIAction;
import com.footballaigame.client.simulationentities.GameState;
import com.footballaigame.client.ais.fsm.entities.Ball;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.utilities.SupportPositionsManager;

import java.util.Random;

/**
 * Represents the FSM AI.
 */
public class FsmAI implements FootballAI {
    
    /**
     * The {@link Random} that is used for generating random numbers.
     */
    public static Random random;
    
    /**
     * The ball.
     */
    public Ball ball;
    
    /**
     * My (AI's) team.
     */
    public Team myTeam;
    
    /**
     * The opponent's team.
     */
    public Team opponentTeam;
    
    /**
     * The support positions manager.
     */
    public SupportPositionsManager supportPositionsManager;
    
    /**
     * Called every time the new match simulation with the AI starts.
     * <p>
     * Called before {@link #getParameters()}.
     */
    @Override
    public void initialize() {
        if (random == null)
            random = new Random();
        supportPositionsManager = new SupportPositionsManager(this);
    }
    
    /**
     * Gets the {@link AIAction} for the specified {@link GameState}.
     *
     * @param gameState The state of the game.
     * @return The {@link AIAction} for the specified {@link GameState}.
     */
    @Override
    public AIAction getAction(GameState gameState) {
        
        if (gameState.step == 0 || myTeam == null) {
            ball = new Ball(gameState.ball);
            myTeam = new Team(getParameters(), this);
            opponentTeam = new Team(getParameters(), this); // expect opponent to have the same parameters
        }
        
        // AI entities (wrappers of simulationentities) are set accordingly
        ball.loadState(gameState);
        opponentTeam.loadState(gameState, false); // must be loaded before my team!
        myTeam.loadState(gameState, true);
        supportPositionsManager.update();
        
        // new action
        AIAction currentAction = new AIAction();
        currentAction.step = gameState.step;
        currentAction.playerActions = myTeam.getActions();
        
        return currentAction;
    }
    
    /**
     * Gets the players' parameters.
     *
     * @return The array of football players with their parameters set.
     */
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
