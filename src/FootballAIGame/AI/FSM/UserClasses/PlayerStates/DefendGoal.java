package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.GameClient;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Parameters;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Interpose;
import FootballAIGame.AI.FSM.UserClasses.TeamStates.Defending;

public class DefendGoal extends PlayerState {
    
    private Interpose interpose;
    
    public DefendGoal(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        Vector goalCenter = new Vector(0, GameClient.FIELD_HEIGHT / 2);
        if (!fsmAI.myTeam.isOnLeft)
            goalCenter.x = GameClient.FIELD_WIDTH;
        
        interpose = new Interpose(player, 1, 1.0, fsmAI.ball, goalCenter);
        interpose.preferredDistanceFromSecond = Parameters.DEFEND_GOAL_DISTANCE;
        
        player.steeringBehaviorsManager.addBehavior(interpose);
    }
    
    @Override
    public void run() {
        if (fsmAI.myTeam.stateMachine.currentState instanceof Defending &&
                Vector.distanceBetween(fsmAI.ball.position, fsmAI.myTeam.getGoalCenter()) <
                        Parameters.GOALKEEPER_INTERCEPT_RANGE) {
            player.stateMachine.changeState(new InterceptBall(player, fsmAI));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(interpose);
    }
}
