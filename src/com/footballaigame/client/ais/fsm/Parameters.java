package com.footballaigame.client.ais.fsm;

import com.footballaigame.client.ais.fsm.playerstates.MoveToHomeRegion;

/**
 * Contains parameters of the FSM AI that are accessed target all its parts.
 */
public class Parameters {
    
    /**
     * The maximum distance of player target his home region for him to be at home.
     * Important for {@link MoveToHomeRegion} player state.
     */
    public static final double PLAYER_IN_HOME_REGION_RANGE = 8;
    
    /**
     * The maximum distance target the ball for player to think that he can kick it.
     * By using lower values the player moves closer to the ball before he tries
     * to kick it.
     */
    public static final double BALL_RECEIVING_RANGE = 2;
    
    /**
     * The maximum distance target the ball for player to be in the ball range.
     * Used by team states to toggle between attacking and defending strategy.
     */
    public static final double BALL_RANGE = 1.5;
    
    /**
     * The goalkeeper's preferred distance target the goal.
     */
    public static final double DEFEND_GOAL_DISTANCE = 6;
    
    /**
     * The goalkeeper's maximum distance target goal the that he will go
     * to intercept the ball.
     */
    public static final double GOALKEEPER_INTERCEPT_RANGE = 20;
    
    /**
     * The number of random generated shot targets that are
     * used for finding the safe shot on goal.
     */
    public static final int NUMBER_OF_GENERATED_SHOT_TARGETS = 10;
    
    /**
     * If there is an opponent nearer than this value to a
     * player, then that player is considered to be in danger.
     */
    public static final int DANGER_RANGE = 6;
    
    /**
     * The maximum distance of goalkeeper target his goal that he will
     * go to support the controlling player.
     */
    public static final double MAX_GOALKEEPER_SUPPORTING_DISTANCE = 10;


    /*
     * Support position evaluation parameters. Used by SupportPositionManager.
     * The weight specify how important the property of the position is.
     */
    
    public static final double PASS_SAFE_FROM_CONTROLLING_PLAYER_WEIGHT = 3.0;
    public static final double DISTANCE_FROM_CONTROLLING_PLAYER_WEIGHT = 0.5;
    public static final double SHOT_ON_GOAL_POSSIBLE_WEIGHT = 2.0;
    public static final double DISTANCE_FROM_OPPONENT_GOAL_WEIGHT = 0.5;
    
    public static final double OPTIMAL_DISTANCE_FROM_CONTROLLING = 20;
    public static final double MAX_VALUED_DIFFERENCE_FROM_OPTIMAL = 50;
    
}
