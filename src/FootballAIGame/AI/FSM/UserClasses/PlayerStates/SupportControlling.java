package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.GoalKeeper;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.PassToPlayerMessage;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.SupportControllingMessage;
import FootballAIGame.AI.FSM.UserClasses.Parameters;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Arrive;
import FootballAIGame.AI.FSM.UserClasses.Utilities.SupportPositionsManager;

public class SupportControlling extends PlayerState {
    
    private Arrive arrive;
    
    public SupportControlling(Player player) {
        super(player);
    }
    
    @Override
    public void enter() {
        arrive = new Arrive(player, 1, 1.0, SupportPositionsManager.getInstance().getBestSupportPosition());
        player.steeringBehaviorsManager.addBehavior(arrive);
        Ai.getInstance().myTeam.supportingPlayers.add(player);
    }
    
    @Override
    public void run() {
        arrive.target = SupportPositionsManager.getInstance().getBestSupportPosition();
        Team team = Ai.getInstance().myTeam;
        
        // nearest except goalkeeper and controlling
        Player nearest = Ai.getInstance().myTeam.getNearestPlayerToPosition(arrive.target, team.goalKeeper, team.controllingPlayer);
        
        // goalkeeper shouldn't go too far from his home region
        if (player instanceof GoalKeeper &&
                Vector.distanceBetween(arrive.target, player.homeRegion.center) > Parameters.MAX_GOALKEEPER_SUPPORTING_DISTANCE) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player));
            return;
        }
        
        // if shot on goal is possible request pass from controlling
        Vector shotVector;
        if ((shotVector = Ai.getInstance().myTeam.tryGetShotOnGoal(player)) != null && team.controllingPlayer != null)
            MessageDispatcher.getInstance().sendMessage(new PassToPlayerMessage(player));
        
        // someone else is nearer the best position (not goalkeeper)
        if (!(player instanceof GoalKeeper) && nearest != player && nearest != team.controllingPlayer) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(arrive);
        Ai.getInstance().myTeam.supportingPlayers.remove(player);
    }
}
