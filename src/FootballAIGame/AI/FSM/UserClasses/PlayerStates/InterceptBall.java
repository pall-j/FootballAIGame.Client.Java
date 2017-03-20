package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Parameters;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Pursuit;
import FootballAIGame.AI.FSM.UserClasses.TeamStates.Attacking;

public class InterceptBall extends PlayerState {
    
    private Pursuit ballPursuit;
    
    public InterceptBall(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        ballPursuit = new Pursuit(player, 1, 1.0, fsmAI.ball);
        player.steeringBehaviorsManager.addBehavior(ballPursuit);
    }
    
    @Override
    public void run() {
        if (fsmAI.myTeam.stateMachine.currentState instanceof Attacking ||
                Vector.distanceBetween(player.position, fsmAI.myTeam.getGoalCenter()) >
                        Parameters.GOALKEEPER_INTERCEPT_RANGE) {
            player.stateMachine.changeState(new DefendGoal(player, fsmAI));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(ballPursuit);
    }
}
