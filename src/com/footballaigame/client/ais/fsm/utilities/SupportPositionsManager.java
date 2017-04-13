package com.footballaigame.client.ais.fsm.utilities;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;
import com.footballaigame.client.simulationentities.FootballBall;
import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.Parameters;

import java.util.ArrayList;
import java.util.List;

public class SupportPositionsManager {
    
    private FsmAI fsmAI;
    
    public SupportPositionsManager(FsmAI fsmAI) {
        createSupportPositions();
        this.fsmAI = fsmAI;
    }
    
    private List<SupportPosition> leftSupportPositions;
    
    private List<SupportPosition> rightSupportPositions;
    
    private List<SupportPosition> supportPositions;
    
    public Vector getBestSupportPosition() {
        
        SupportPosition bestPosition = supportPositions.get(0);
        for (SupportPosition supportPosition : supportPositions) {
            if (supportPosition.score > bestPosition.score)
                bestPosition = supportPosition;
        }
        
        return bestPosition.position;
    }
    
    public void update() {
        for (SupportPosition supportPosition : supportPositions)
            updatePosition(supportPosition);
    }
    
    private void updatePosition(SupportPosition supportPosition) {
        supportPosition.score = 0;
        
        // these other scores may be used for debugging
        supportPosition.shootScore = 0;
        supportPosition.distanceScore = 0;
        supportPosition.passScore = 0;
        
        Player controlling = fsmAI.myTeam.controllingPlayer;
        if (controlling != null) {
            if (fsmAI.myTeam.isPassFromControllingSafe(supportPosition.position)) {
                supportPosition.score += Parameters.PASS_SAFE_FROM_CONTROLLING_PLAYER_WEIGHT;
                supportPosition.passScore += Parameters.PASS_SAFE_FROM_CONTROLLING_PLAYER_WEIGHT;
            }
            
            supportPosition.score += Parameters.DISTANCE_FROM_CONTROLLING_PLAYER_WEIGHT *
                    getDistanceFromControllingScore(supportPosition.position);
            supportPosition.distanceScore += Parameters.DISTANCE_FROM_CONTROLLING_PLAYER_WEIGHT *
                    getDistanceFromControllingScore(supportPosition.position);
        }
        
        if (isShotOnGoalPossible(supportPosition.position)) {
            supportPosition.score += Parameters.SHOT_ON_GOAL_POSSIBLE_WEIGHT;
            supportPosition.shootScore += Parameters.SHOT_ON_GOAL_POSSIBLE_WEIGHT;
        }
        
        // distance from opponent
        
        
    }
    
    private boolean isShotOnGoalPossible(Vector position) {
        // we expect the lowest possible max kicking power of the player
    
        FootballPlayer artificialPlayer = new FootballPlayer(0);
        artificialPlayer.position = position;
        
        FootballBall artificialBall = new FootballBall();
        artificialBall.position = position;
    
        return (fsmAI.myTeam.tryGetShotOnGoal(artificialPlayer, artificialBall) != null);
    }
    
    private double getDistanceFromControllingScore(Vector position) {
        double distance = Vector.distanceBetween(position, fsmAI.myTeam.controllingPlayer.position);
        
        double diff = Math.abs(distance - Parameters.OPTIMAL_DISTANCE_FROM_CONTROLLING);
        
        if (diff <= Parameters.MAX_VALUED_DIFFERENCE_FROM_OPTIMAL)
            return (Parameters.MAX_VALUED_DIFFERENCE_FROM_OPTIMAL - diff) / Parameters.MAX_VALUED_DIFFERENCE_FROM_OPTIMAL;
        
        return 0;
    }
    
    private void createSupportPositions() {
        leftSupportPositions = new ArrayList<SupportPosition>();
        rightSupportPositions = new ArrayList<SupportPosition>();
        supportPositions = new ArrayList<SupportPosition>();
        
        double dx = GameClient.FIELD_WIDTH / 15.0;
        double dy = GameClient.FIELD_HEIGHT / 9.0;
        
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                leftSupportPositions.add(new SupportPosition(new Vector(i * dx, j * dy), 0));
                rightSupportPositions.add(new SupportPosition(new Vector(i * dx + 6 * dx, j * dy), 0));
            }
        }
        
        for (SupportPosition leftSupportPosition : leftSupportPositions) {
            supportPositions.add(leftSupportPosition);
        }
        
        for (SupportPosition rightSupportPosition : rightSupportPositions) {
            supportPositions.add(rightSupportPosition);
        }
    }
    
    public class SupportPosition {
        
        public Vector position;
        
        public double score;
        
        public double distanceScore;
        
        public double shootScore;
        
        public double passScore;
        
        public SupportPosition(Vector position, double score) {
            this.position = position;
            this.score = score;
        }
    }
}

