package FootballAIGame.AI.FSM.UserClasses.TeamStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Region;
import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.SupportControllingMessage;

public class Attacking extends TeamState {
    
    public Attacking(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
    }
    
    @Override
    public void enter() {
        setHomeRegions();
    }
    
    @Override
    public void setHomeRegions() {
        
        team.goalKeeper.homeRegion = Region.getRegion(7, 4);
        
        team.defenders.get(0).homeRegion = Region.getRegion(6, 4);
        team.defenders.get(1).homeRegion = Region.getRegion(5, 3);
        team.defenders.get(2).homeRegion = Region.getRegion(4, 2);
        team.defenders.get(3).homeRegion = Region.getRegion(4, 6);
        
        team.midfielders.get(0).homeRegion = Region.getRegion(3, 4);
        team.midfielders.get(1).homeRegion = Region.getRegion(2, 2);
        team.midfielders.get(2).homeRegion = Region.getRegion(2, 4);
        team.midfielders.get(3).homeRegion = Region.getRegion(2, 6);
        
        team.forwards.get(0).homeRegion = Region.getRegion(1, 2);
        team.forwards.get(1).homeRegion = Region.getRegion(1, 6);
        
        if (!team.isOnLeft) return;
        
        // team is on the left side -> mirror image
        for (Player player : team.players) {
            player.homeRegion = Region.getRegion(
                    (Region.NUMBER_OF_COLUMNS - 1) - player.homeRegion.x, player.homeRegion.y);
        }
        
    }
    
    @Override
    public void run() {
        if (team.playerInBallRange == null &&
                fsmAI.opponentTeam.playerInBallRange != null) {
            team.stateMachine.changeState(new Defending(team, fsmAI));
        }
        
        if (team.supportingPlayers.size() == 0 && team.controllingPlayer != null) {
            Vector bestPos = fsmAI.supportPositionsManager.getBestSupportPosition();
            Player bestSupporter = team.getNearestPlayerToPosition(bestPos, team.controllingPlayer);
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), bestSupporter);
        }
    }
    
}
