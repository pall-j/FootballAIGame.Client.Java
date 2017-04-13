package com.footballaigame.client.ais.fsm;

public class Parameters {
    
    public static final double PLAYER_IN_HOME_REGION_RANGE = 8;
    public static final double BALL_RECEIVING_RANGE = 2;
    public static final double BALL_RANGE = 1.5;
    public static final double DEFEND_GOAL_DISTANCE = 6;
    public static final double GOALKEEPER_INTERCEPT_RANGE = 20;
    public static final int NUMBER_OF_GENERATED_SHOT_TARGETS = 10;
    public static final int DANGER_RANGE = 6;
    public static final double MAX_GOALKEEPER_SUPPORTING_DISTANCE = 10;

        /* Support position evaluation parameters */
    
    public static final double PASS_SAFE_FROM_CONTROLLING_PLAYER_WEIGHT = 3.0;
    public static final double DISTANCE_FROM_CONTROLLING_PLAYER_WEIGHT = 0.5;
    public static final double SHOT_ON_GOAL_POSSIBLE_WEIGHT = 2.0;
    public static final double DISTANCE_FROM_OPPONENT_GOAL_WEIGHT = 0.5;
    
    public static final double OPTIMAL_DISTANCE_FROM_CONTROLLING = 20;
    public static final double MAX_VALUED_DIFFERENCE_FROM_OPTIMAL = 50;
    
}
