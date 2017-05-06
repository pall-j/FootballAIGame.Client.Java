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

public class Team {
    
    private boolean initialEnter;
    
    protected FsmAI fsmAI;
    
    public FiniteStateMachine<Team> stateMachine;
    
    public Player[] players;
    
    public GoalKeeper goalKeeper;
    
    public List<Defender> defenders;
    
    public List<Midfielder> midfielders;
    
    public List<Forward> forwards;
    
    public Player playerInBallRange;
    
    public Player controllingPlayer;
    
    public Player passReceiver;
    
    public List<Player> supportingPlayers;
    
    /**
     * Indicates whether the team holds currently the left goal post.
     */
    public boolean isOnLeft;
    
    public Vector getGoalCenter() {
        return isOnLeft
                ? new Vector(0, GameClient.FIELD_HEIGHT / 2)
                : new Vector(GameClient.FIELD_WIDTH, GameClient.FIELD_HEIGHT / 2);
    }
    
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
    
    public Player getNearestPlayerToBall() {
        return getNearestPlayerToPosition(fsmAI.ball.position);
    }
    
    public boolean isNearerToOpponent(Player player, Player otherPlayer) {
        if (isOnLeft)
            return player.position.x > otherPlayer.position.x;
        else
            return player.position.x < otherPlayer.position.x;
    }
    
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
    
    public void processMessage(Message message) {
        stateMachine.processMessage(this, message);
    }
    
    public boolean isPassFromControllingSafe(Vector target) {
        return isKickSafe(controllingPlayer, target);
    }
    
    public boolean isKickSafe(FootballPlayer from, Vector target) {
        
        Ball ball = fsmAI.ball;
        
        if (from == null)
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
            
            double t1 = ball.getTimeToCoverDistance(ballToInterposeDist, from.getMaxKickSpeed());
            double t2 = opponent.getTimeToGetToTarget(kickablePosition);
            
            if (t2 < t1)
                return false;
        }
        
        return true;
    }
    
    public boolean isKickPossible(FootballPlayer player, Vector target, FootballBall ball) {
        return !Double.isInfinite(
                ball.getTimeToCoverDistance(Vector.getDistanceBetween(ball.position, target), player.getMaxKickSpeed()));
    }
    
    public Vector tryGetShotOnGoal(FootballPlayer player) {
        return tryGetShotOnGoal(player, fsmAI.ball);
    }
    
    public Vector tryGetShotOnGoal(FootballPlayer player, FootballBall ball) {
        
        for (int i = 0; i < Parameters.NUMBER_OF_GENERATED_SHOT_TARGETS; i++) {
            Vector target =
                    new Vector(0, GameClient.FIELD_HEIGHT / 2.0 + (FsmAI.random.nextDouble() - 0.5) * 7.32 / 2);
            if (isOnLeft)
                target.x = GameClient.FIELD_WIDTH;
            
            if (isKickPossible(player, target, ball) && isKickSafe(player, target)) {
                return target;
            }
        }
        
        return null;
    }
    
    public Player tryGetSafePass(Player player) {
        return tryGetSafePass(player, fsmAI.ball);
    }
    
    public Player tryGetSafePass(Player player, Ball ball) {
        Player target = null;
        
        for (Player otherPlayer : players) {
            if (player == otherPlayer)
                continue;
            
            if (isKickPossible(player, otherPlayer.position, ball) && isKickSafe(player, otherPlayer.position)) {
                if (target == null || (isOnLeft && target.position.x < otherPlayer.position.x) ||
                        (!isOnLeft && target.position.x > otherPlayer.position.x))
                    target = otherPlayer;
            }
        }
        
        return target;
    }
    
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
