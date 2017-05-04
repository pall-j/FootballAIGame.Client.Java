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
 * The main FsmAI class where the AI behavior is defined.
 */
public class FsmAI implements FootballAI {
    
    public static Random random;
    
    public Ball ball;
    
    public Team myTeam;
    
    public Team opponentTeam;
    
    public SupportPositionsManager supportPositionsManager;
    
    @Override
    public void initialize() {
        if (random == null)
            random = new Random();
        supportPositionsManager = new SupportPositionsManager(this);
    }
    
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
