package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.GameClient;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Parameters;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Interpose;
import FootballAIGame.AI.FSM.UserClasses.TeamStates.Defending;

public class DefendGoal extends PlayerState {
    
    private Interpose interpose;
    
    public DefendGoal(Player player) {
        super(player);
    }
    
    @Override
    public void enter() {
        Vector goalCenter = new Vector(0, GameClient.FIELD_HEIGHT / 2);
        if (!Ai.getInstance().myTeam.isOnLeft)
            goalCenter.x = GameClient.FIELD_WIDTH;
        
        interpose = new Interpose(player, 1, 1.0, Ai.getInstance().ball, goalCenter);
        interpose.preferredDistanceFromSecond = Parameters.DEFEND_GOAL_DISTANCE;
        
        player.steeringBehaviorsManager.addBehavior(interpose);
    }
    
    @Override
    public void run() {
        if (Ai.getInstance().myTeam.stateMachine.currentState instanceof Defending &&
                Vector.distanceBetween(Ai.getInstance().ball.position, Ai.getInstance().myTeam.getGoalCenter()) <
                        Parameters.GOALKEEPER_INTERCEPT_RANGE) {
            player.stateMachine.changeState(new MoveToHomeRegion(player));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(interpose);
    }
}
