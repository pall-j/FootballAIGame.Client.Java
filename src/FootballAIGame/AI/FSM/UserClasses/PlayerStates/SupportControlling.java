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

public class SupportControlling extends PlayerState {
    
    private Arrive arrive;
    
    public SupportControlling(Player player, Ai ai) {
        super(player, ai);
    }
    
    @Override
    public void enter() {
        arrive = new Arrive(player, 1, 1.0, ai.supportPositionsManager.getBestSupportPosition());
        player.steeringBehaviorsManager.addBehavior(arrive);
        ai.myTeam.supportingPlayers.add(player);
    }
    
    @Override
    public void run() {
        arrive.target = ai.supportPositionsManager.getBestSupportPosition();
        Team team = ai.myTeam;
        
        // nearest except goalkeeper and controlling
        Player nearest = ai.myTeam.getNearestPlayerToPosition(arrive.target, team.goalKeeper, team.controllingPlayer);
        
        // goalkeeper shouldn't go too far from his home region
        if (player instanceof GoalKeeper &&
                Vector.distanceBetween(arrive.target, player.homeRegion.center) > Parameters.MAX_GOALKEEPER_SUPPORTING_DISTANCE) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, ai));
            return;
        }
        
        // if shot on goal is possible request pass from controlling
        if (ai.myTeam.tryGetShotOnGoal(player) != null && team.controllingPlayer != null)
            MessageDispatcher.getInstance().sendMessage(new PassToPlayerMessage(player));
        
        // someone else is nearer the best position (not goalkeeper)
        if (!(player instanceof GoalKeeper) && nearest != player && nearest != team.controllingPlayer) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, ai));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(arrive);
        ai.myTeam.supportingPlayers.remove(player);
    }
}
