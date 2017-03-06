package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.GoalKeeper;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.PassToPlayerMessage;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Wander;

public class Default extends PlayerState {
    
    private Wander wander;
    
    public Default(Player player) {
        super(player);
    }
    
    @Override
    public void enter() {
        
        wander = new Wander(player, 1, 0.2, 0, 2, 4);
        
        player.steeringBehaviorsManager.addBehavior(wander);
    }
    
    @Override
    public void run() {
        Player controlling = Ai.getInstance().myTeam.controllingPlayer;
        Team team = Ai.getInstance().myTeam;
        
        if (player instanceof GoalKeeper) {
            player.stateMachine.changeState(new DefendGoal(player));
            return;
        }
        
        if (controlling != null &&
                team.isNearerToOpponent(player, controlling) &&
                team.isPassFromControllingSafe(player.position) &&
                team.passReceiver == null) {
            MessageDispatcher.getInstance().sendMessage(new PassToPlayerMessage(player), controlling);
        } else if (!player.isAtHomeRegion()) {
            player.stateMachine.changeState(new MoveToHomeRegion(player));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(wander);
    }
}
