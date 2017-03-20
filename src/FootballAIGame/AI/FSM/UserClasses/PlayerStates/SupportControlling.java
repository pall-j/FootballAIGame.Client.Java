package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
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
    
    public SupportControlling(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        arrive = new Arrive(player, 1, 1.0, fsmAI.supportPositionsManager.getBestSupportPosition());
        player.steeringBehaviorsManager.addBehavior(arrive);
        fsmAI.myTeam.supportingPlayers.add(player);
    }
    
    @Override
    public void run() {
        arrive.target = fsmAI.supportPositionsManager.getBestSupportPosition();
        Team team = fsmAI.myTeam;
        
        // nearest except goalkeeper and controlling
        Player nearest = fsmAI.myTeam.getNearestPlayerToPosition(arrive.target, team.goalKeeper, team.controllingPlayer);
        
        // goalkeeper shouldn't go too far from his home region
        if (player instanceof GoalKeeper &&
                Vector.distanceBetween(arrive.target, player.homeRegion.center) > Parameters.MAX_GOALKEEPER_SUPPORTING_DISTANCE) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        // if shot on goal is possible request pass from controlling
        if (fsmAI.myTeam.tryGetShotOnGoal(player) != null && team.controllingPlayer != null)
            MessageDispatcher.getInstance().sendMessage(new PassToPlayerMessage(player));
        
        // someone else is nearer the best position (not goalkeeper)
        if (!(player instanceof GoalKeeper) && nearest != player && nearest != team.controllingPlayer) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, fsmAI));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(arrive);
        fsmAI.myTeam.supportingPlayers.remove(player);
    }
}
