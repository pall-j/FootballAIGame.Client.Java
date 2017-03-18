package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Parameters;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Pursuit;
import FootballAIGame.AI.FSM.UserClasses.TeamStates.Attacking;

public class InterceptBall extends PlayerState {
    
    private Pursuit ballPursuit;
    
    public InterceptBall(Player player, Ai ai) {
        super(player, ai);
    }
    
    @Override
    public void enter() {
        ballPursuit = new Pursuit(player, 1, 1.0, ai.ball);
        player.steeringBehaviorsManager.addBehavior(ballPursuit);
    }
    
    @Override
    public void run() {
        if (ai.myTeam.stateMachine.currentState instanceof Attacking ||
                Vector.distanceBetween(player.position, ai.myTeam.getGoalCenter()) >
                        Parameters.GOALKEEPER_INTERCEPT_RANGE) {
            player.stateMachine.changeState(new DefendGoal(player, ai));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(ballPursuit);
    }
}
