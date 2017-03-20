package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Ball;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Parameters;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Arrive;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Pursuit;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.SteeringBehavior;

public class ReceivePass extends PlayerState {
    
    private SteeringBehavior steeringBehavior;
    
    private Vector passTarget;
    
    public ReceivePass(Player player, FsmAI fsmAI, Vector passTarget) {
        super(player, fsmAI);
        this.passTarget = passTarget;
    }
    
    @Override
    public void enter() {
        fsmAI.myTeam.passReceiver = player;
        fsmAI.myTeam.controllingPlayer = player;
        steeringBehavior = new Arrive(player, 1, 1.0, passTarget);
        player.steeringBehaviorsManager.addBehavior(steeringBehavior);
    }
    
    @Override
    public void run() {
        if (fsmAI.myTeam.passReceiver != player) {
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        // lost control
        if (fsmAI.opponentTeam.playerInBallRange != null && fsmAI.myTeam.playerInBallRange == null) {
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        if (player.canKickBall(fsmAI.ball)) {
            player.stateMachine.changeState(new KickBall(player, fsmAI));
            return;
        }
        
        if (Vector.distanceBetween(fsmAI.ball.position, player.position) < Parameters.BALL_RECEIVING_RANGE) {
            player.stateMachine.changeState(new PursueBall(player, fsmAI));
            return;
        }
        
        updatePassTarget();
        
        Player nearestOpponent = fsmAI.opponentTeam.getNearestPlayerToPosition(player.position);
        Ball ball = fsmAI.ball;
        
        double timeToReceive = ball.timeToCoverDistance(Vector.distanceBetween(ball.position, passTarget), ball.currentSpeed());
        
        if (nearestOpponent.timeToGetToTarget(passTarget) < timeToReceive ||
                player.timeToGetToTarget(passTarget) > timeToReceive) {
            if (steeringBehavior instanceof Arrive) {
                player.steeringBehaviorsManager.removeBehavior(steeringBehavior);
                steeringBehavior = new Pursuit(player, steeringBehavior.priority, steeringBehavior.weight, ball);
                player.steeringBehaviorsManager.addBehavior(steeringBehavior);
            }
        } else {
            if (steeringBehavior instanceof Pursuit) {
                player.steeringBehaviorsManager.removeBehavior(steeringBehavior);
                steeringBehavior = new Arrive(player, steeringBehavior.priority, steeringBehavior.weight, passTarget);
                player.steeringBehaviorsManager.addBehavior(steeringBehavior);
            }
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(steeringBehavior);
        if (player == fsmAI.myTeam.passReceiver)
            fsmAI.myTeam.passReceiver = null;
    }
    
    private void updatePassTarget() {
        Ball ball = fsmAI.ball;
        double time = ball.timeToCoverDistance(Vector.distanceBetween(passTarget, ball.position), ball.currentSpeed());
        
        passTarget = ball.predictedPositionInTime(time);
    
        if (steeringBehavior instanceof Arrive)
            ((Arrive)steeringBehavior).target = passTarget;
    }
}
