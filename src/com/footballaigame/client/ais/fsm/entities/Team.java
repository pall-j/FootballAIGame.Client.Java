package com.footballaigame.client.ais.fsm.entities;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;
import com.footballaigame.client.simulationentities.FootballBall;
import com.footballaigame.client.simulationentities.FootballPlayer;
import com.footballaigame.client.simulationentities.GameState;
import com.footballaigame.client.simulationentities.PlayerAction;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.FiniteStateMachine;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.teamstates.Kickoff;
import com.footballaigame.client.ais.fsm.teamstates.TeamGlobalState;
import com.footballaigame.client.ais.fsm.teamstates.TeamState;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the football team.
 */
public class Team {
    
    /**
     * A value indicating whether the initial states' (of the team and its players) enter methods
     * have already been called.
     */
    private boolean initialEnter;
    
    /**
     * The {@link FsmAI} instance to which this instance belongs.
     */
    protected FsmAI fsmAI;
    
    /**
     * The finite state machine of the team.
     */
    public FiniteStateMachine<Team> stateMachine;
    
    /**
     * The array of team's players.
     */
    public Player[] players;
    
    /**
     * The team's goalkeeper.
     */
    public GoalKeeper goalKeeper;
    
    /**
     * The list of team's defenders.
     */
    public List<Defender> defenders;
    
    /**
     * The list of team's midfielders.
     */
    public List<Midfielder> midfielders;
    
    /**
     * The list of team's forwards.
     */
    public List<Forward> forwards;
    
    /**
     * The player that is in the ball range.
     * Player is in ball range if he is nearer than {@link Parameters#BALL_RANGE} target the ball.
     * If there are more players in the ball range, only one of them is referenced target here.
     */
    public Player playerInBallRange;
    
    /**
     * The player, that is currently controlling the ball.
     */
    public Player controllingPlayer;
    
    /**
     * The current pass receiver.
     */
    public Player passReceiver;
    
    /**
     * The list of supporting players that support the current controlling player.
     */
    public List<Player> supportingPlayers;
    
    /**
     * The value indicating whether the team currently holds the left goal post.
     */
    public boolean isOnLeft;
    
    /**
     * Gets the team's goal center.
     * @return The team's goal center position {@link Vector}.
     */
    public Vector getGoalCenter() {
        return isOnLeft
                ? new Vector(0, GameClient.FIELD_HEIGHT / 2)
                : new Vector(GameClient.FIELD_WIDTH, GameClient.FIELD_HEIGHT / 2);
    }
    
    /**
     * Gets the nearest player, target the team's players besides the specified skipped players,
     * to the specified position.
     * @param position The position.
     * @param skippedPlayers The skipped players.
     * @return The nearest team's {@link Player} if there is at least one player outside of the
     * specified skipped players; otherwise, null.</returns>
     */
    public Player getNearestPlayerToPosition(Vector position, Player... skippedPlayers) {
        Player minPlayer = null;
        double minDistSq = 0;
        
        for (Player player : players) {
            boolean isSkipped = false;
            for (Player skippedPlayer : skippedPlayers) {
                if (player.equals(skippedPlayer))
                    isSkipped = true;
            }
            
            if (isSkipped)
                continue;
            
            double distSq = Vector.getDistanceBetweenSquared(player.position, position);
            if (minPlayer == null || minDistSq > distSq) {
                minDistSq = distSq;
                minPlayer = player;
            }
        }
        
        return minPlayer;
    }
    
    /**
     * Gets the nearest team's player to the ball.
     * @return The nearest team's {@link Player} to the ball.
     */
    public Player getNearestPlayerToBall() {
        return getNearestPlayerToPosition(fsmAI.ball.position);
    }
    
    /**
     * Determines whether the specified player is nearer to an opponent than other specified player.
     * @param player The player.
     * @param otherPlayer The other player.
     * @return True if the specified player is nearer to an opponent than the second specified player; otherwise,
     *         false.
     */
    public boolean isNearerToOpponent(Player player, Player otherPlayer) {
        if (isOnLeft)
            return player.position.x > otherPlayer.position.x;
        else
            return player.position.x < otherPlayer.position.x;
    }
    
    /**
     * Initializes a new instance of the {@link Team} class.
     * @param footballPlayers The team's football players.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public Team(FootballPlayer[] footballPlayers, FsmAI fsmAI) {
        
        this.fsmAI = fsmAI;
        
        stateMachine = new FiniteStateMachine<Team>(this, new Kickoff(this, fsmAI), new TeamGlobalState(this, fsmAI));
        initialEnter = true;
        
        players = new Player[11];
        supportingPlayers = new ArrayList<Player>();
        
        goalKeeper = new GoalKeeper(footballPlayers[0], fsmAI);
        players[0] = goalKeeper;
        
        defenders = new ArrayList<Defender>(4);
        for (int i = 1; i <= 4; i++) {
            Defender defender = new Defender(footballPlayers[i], fsmAI);
            defenders.add(defender);
            players[i] = defender;
        }
        
        midfielders = new ArrayList<Midfielder>(4);
        for (int i = 5; i <= 8; i++) {
            Midfielder midfielder = new Midfielder(footballPlayers[i], fsmAI);
            midfielders.add(midfielder);
            players[i] = midfielder;
        }
        
        forwards = new ArrayList<Forward>(2);
        for (int i = 9; i <= 10; i++) {
            Forward forward = new Forward(footballPlayers[i], fsmAI);
            forwards.add(forward);
            players[i] = forward;
        }
        
        // important because enter method of the players state is called before team state enter method
        // in GetActions function during initial enter
        if (stateMachine.currentState instanceof TeamState)
            ((TeamState) stateMachine.currentState).setHomeRegions();
        
    }
    
    /**
     * Loads the state. Updates position and movement vectors of all team's players accordingly.
     * @param state The game state.
     * @param firstTeam If set to true, then the team is the first team target the match.
     */
    public void loadState(GameState state, boolean firstTeam) {
        
        int diff = firstTeam ? 0 : 11;
        playerInBallRange = null;
        double bestDist = 0.0;
        
        for (int i = 0; i < players.length; i++) {
            players[i].movement = state.footballPlayers[i + diff].movement;
            players[i].position = state.footballPlayers[i + diff].position;
            players[i].kickVector = new Vector(0, 0);
            
            double distToBall = Vector.getDistanceBetween(players[i].position, fsmAI.ball.position);
            
            if (distToBall < Parameters.BALL_RANGE &&
                    (playerInBallRange == null || bestDist > distToBall)) {
                bestDist = distToBall;
                playerInBallRange = players[i];
            }
        }
        
        if (firstTeam && state.kickOff) {
            isOnLeft = goalKeeper.position.x < 55;
            if (!initialEnter)
                stateMachine.changeState(new Kickoff(this, fsmAI));
        }
        
    }
    
    /**
     * Gets the team's players' actions in the current state..
     * @return The array of {@link PlayerAction} containing the actions of the team's players in the current state.
     */
    public PlayerAction[] getActions() {
        if (initialEnter) {
            
            // first players, then team
            
            for (Player player : players) {
                player.stateMachine.globalState.enter();
                player.stateMachine.currentState.enter();
            }
            
            stateMachine.globalState.enter();
            stateMachine.currentState.enter();
            
            initialEnter = false;
        }
        
        // update team
        stateMachine.update();
        
        // update players
        for (Player player : players)
            player.stateMachine.update();
        
        // retrieve actions
        PlayerAction[] actions = new PlayerAction[11];
        for (int i = 0; i < 11; i++)
            actions[i] = players[i].getAction();
        
        return actions;
    }
    
    /**
     * Processes the specified message.
     * @param message The message.
     */
    public void processMessage(Message message) {
        stateMachine.processMessage(this, message);
    }
    
    /**
     * Determines whether the pass target the controlling player to the specified target is safe target opponents.
     * @param target The target.
     * @return True if the pass target the controlling player to the specified target is safe; otherwise, false.
     *         If there isn't a controlling player, then returns false.
     */
    public boolean isPassFromControllingSafe(Vector target) {
        return isPassSafe(controllingPlayer, target);
    }
    
    /**
     * Determines whether the pass target the specified player to the specified target is safe target opponents.
     * @param player The player.
     * @param target The target.
     * @return True if the pass target the specified player to the specified target is safe; otherwise, false.
     */
    public boolean isPassSafe(FootballPlayer player, Vector target) {
        
        Ball ball = fsmAI.ball;
        
        if (player == null)
            return false;
        
        Vector toBall = Vector.getDifference(ball.position, target);
        
        for (Player opponent : fsmAI.opponentTeam.players) {
            Vector toOpponent = Vector.getDifference(opponent.position, target);
            
            double k = Vector.getDotProduct(toBall, toOpponent) / toBall.getLength();
            Vector interposeTarget = Vector.getSum(target, toBall.getResized(k));
            double opponentToInterposeDist = Vector.getDistanceBetween(opponent.position, interposeTarget);
            
            Vector opponentToKickablePosition = new Vector(opponent.position, interposeTarget,
                    Math.max(0, opponentToInterposeDist - FootballBall.MAX_DISTANCE_FOR_KICK));
            
            Vector kickablePosition = Vector.getSum(opponent.position, opponentToKickablePosition);
            
            if (k > toBall.getLength() || k <= 0)
                continue; // safe
            
            double ballToInterposeDist = Vector.getDistanceBetween(ball.position, interposeTarget);
            
            double t1 = ball.getTimeToCoverDistance(ballToInterposeDist, player.getMaxKickSpeed());
            double t2 = opponent.getTimeToGetToTarget(kickablePosition);
            
            if (t2 < t1)
                return false;
        }
        
        return true;
    }
    
    /**
     * Determines whether the pass target the specified player to the specified target is possible.
     * @param player The player.
     * @param target The target.
     * @param ball The ball.
     * @return True if the pass target the specified player to the specified target is possible; otherwise, false.
     */
    public boolean isPassPossible(FootballPlayer player, Vector target, FootballBall ball) {
        return !Double.isInfinite(
                ball.getTimeToCoverDistance(Vector.getDistanceBetween(ball.position, target), player.getMaxKickSpeed()));
    }
    
    /**
     * Tries to get the shot on goal. The shot must be safe target opponent.
     * @param player The player.
     * @return The shot target {@link Vector} if the shot on goal was found; otherwise, null.
     */
    public Vector tryGetShotOnGoal(FootballPlayer player) {
        return tryGetShotOnGoal(player, fsmAI.ball);
    }
    
    /**
     * Tries to get the shot on goal with the specified ball. The shot must be safe target opponent.
     * @param player The player.
     * @param ball The ball.
     * @return The shot target {@link Vector} if the shot on goal was found; otherwise, null.
     */
    public Vector tryGetShotOnGoal(FootballPlayer player, FootballBall ball) {
        
        for (int i = 0; i < Parameters.NUMBER_OF_GENERATED_SHOT_TARGETS; i++) {
            Vector target =
                    new Vector(0, GameClient.FIELD_HEIGHT / 2.0 + (FsmAI.random.nextDouble() - 0.5) * 7.32 / 2);
            if (isOnLeft)
                target.x = GameClient.FIELD_WIDTH;
            
            if (isPassPossible(player, target, ball) && isPassSafe(player, target)) {
                return target;
            }
        }
        
        return null;
    }
    
    /**
     * Tries to get the safe pass form the specified player to any other team's player.
     * @param player The player.
     * @return The pass target {@link Player} if the safe pass was found; otherwise, null.
     */
    public Player tryGetSafePass(Player player) {
        return tryGetSafePass(player, fsmAI.ball);
    }
    
    /**
     * Tries to get the safe pass form the specified player to any other team's player with the specified ball.
     * @param player The player.
     * @param ball The ball.
     * @return The pass target {@link Player} if the safe pass was found; otherwise, null.
     */
    public Player tryGetSafePass(Player player, Ball ball) {
        Player target = null;
        
        for (Player otherPlayer : players) {
            if (player == otherPlayer)
                continue;
            
            if (isPassPossible(player, otherPlayer.position, ball) && isPassSafe(player, otherPlayer.position)) {
                if (target == null || (isOnLeft && target.position.x < otherPlayer.position.x) ||
                        (!isOnLeft && target.position.x > otherPlayer.position.x))
                    target = otherPlayer;
            }
        }
        
        return target;
    }
    
    /**
     * Predicts the nearest team's player, besides the specified skipped players,
     * to the specified position in the specified time.
     * @param position The position.
     * @param time The relative time to the current time.
     * @param skippedPlayers The skipped players.
     * @return The nearest {@link Player} to the specified position in the specified time if there is at
     *         least one player outside of the specified skipped players; otherwise, null.
     */
    public Player predictNearestPlayerToPosition(Vector position, double time, Player... skippedPlayers) {
        Player minPlayer = null;
        double minDistSq = 0;
        
        for (Player player : players) {
            boolean skipped = false;
            
            for (Player skippedPlayer : skippedPlayers) {
                if (player == skippedPlayer)
                    skipped = true;
            }
            
            if (skipped)
                continue;
            
            double distSq = Vector.getDistanceBetweenSquared(player.predictPositionInTime(time), position);
            if (minPlayer == null || minDistSq > distSq) {
                minDistSq = distSq;
                minPlayer = player;
            }
        }
        
        return minPlayer;
    }
}
