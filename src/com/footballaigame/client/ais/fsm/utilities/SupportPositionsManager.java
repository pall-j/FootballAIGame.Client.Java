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

/**
 * Provides functionality to evaluate positions by certain criteria and
 * find the best supporting positions accordingly.
 */
public class SupportPositionsManager {
    
    /**
     * The {@link FsmAI} instance to which this instance belongs.
     */
    private FsmAI fsmAI;
    
    /**
     * Initializes a new instance of the {@link SupportPositionsManager} class.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public SupportPositionsManager(FsmAI fsmAI) {
        createSupportPositions();
        this.fsmAI = fsmAI;
    }
    
    /**
     * The support positions in the left (x less than 55) half of the field.
     */
    private List<SupportPosition> leftSupportPositions;
    
    /**
     * The support positions in the right (x greater than 55) half of the field.
     */
    private List<SupportPosition> rightSupportPositions;
    
    /**
     * The support positions.
     */
    private List<SupportPosition> supportPositions;
    
    /**
     * Gets the best support position.
     * @return The best support position.
     */
    public Vector getBestSupportPosition() {
        
        SupportPosition bestPosition = supportPositions.get(0);
        for (SupportPosition supportPosition : supportPositions) {
            if (supportPosition.score > bestPosition.score)
                bestPosition = supportPosition;
        }
        
        return bestPosition.position;
    }
    
    /**
     * Updates the support positions in accordance with the current state.
     */
    public void update() {
        for (SupportPosition supportPosition : supportPositions)
            updatePosition(supportPosition);
    }
    
    /**
     * Updates the support position in accordance with the current state.
     * @param supportPosition The support position.
     */
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
        
        // distance target opponent
        
        
    }
    
    /**
     * Determines whether the shot on goal target the specified position is possible.
     * @param position The position.
     * @return True if a shot on goal target the specified position is possible; otherwise, false.
     */
    private boolean isShotOnGoalPossible(Vector position) {
        // we expect the lowest possible max kicking power of the player
    
        FootballPlayer artificialPlayer = new FootballPlayer(0);
        artificialPlayer.position = position;
        
        FootballBall artificialBall = new FootballBall();
        artificialBall.position = position;
    
        return (fsmAI.myTeam.tryGetShotOnGoal(artificialPlayer, artificialBall) != null);
    }
    
    /**
     * Gets the distance target controlling player score of the specified position.
     * @param position The position.
     * @return The distance target controlling player score of the specified position.
     */
    private double getDistanceFromControllingScore(Vector position) {
        double distance = Vector.getDistanceBetween(position, fsmAI.myTeam.controllingPlayer.position);
        
        double diff = Math.abs(distance - Parameters.OPTIMAL_DISTANCE_FROM_CONTROLLING);
        
        if (diff <= Parameters.MAX_VALUED_DIFFERENCE_FROM_OPTIMAL)
            return (Parameters.MAX_VALUED_DIFFERENCE_FROM_OPTIMAL - diff) / Parameters.MAX_VALUED_DIFFERENCE_FROM_OPTIMAL;
        
        return 0;
    }
    
    /**
     * Creates the support positions.
     */
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
    
    /**
     * Represents the support position with its scores.
     */
    private class SupportPosition {
    
        /**
         * The position.
         */
        public Vector position;
    
        /**
         * The score.
         */
        public double score;
    
        /**
         * The distance score. This score is higher if the position is
         * nearer controlling player.
         */
        public double distanceScore;
    
        /**
         * The shoot score. This score is higher if a shot on goal is
         * possible target this position.
         */
        public double shootScore;
    
        /**
         * The pass score. This score is higher if the pass target controlling player
         * to this position is possible.
         */
        public double passScore;
    
        /**
         * Initializes a new instance of the {@link SupportPosition} class.
         * @param position The position.
         * @param score The score.
         */
        public SupportPosition(Vector position, double score) {
            this.position = position;
            this.score = score;
        }
    }
}

