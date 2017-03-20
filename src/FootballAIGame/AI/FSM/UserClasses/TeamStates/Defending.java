package FootballAIGame.AI.FSM.UserClasses.TeamStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Region;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Interpose;

import java.util.ArrayList;
import java.util.List;

public class Defending extends TeamState {
    
    private List<Interpose> Interposes;
    
    public Defending(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
    }
    
    @Override
    public void setHomeRegions() {
        
        team.goalKeeper.homeRegion = Region.getRegion(0, 4);
        
        team.defenders.get(0).homeRegion = Region.getRegion(1, 1);
        team.defenders.get(1).homeRegion = Region.getRegion(1, 3);
        team.defenders.get(2).homeRegion = Region.getRegion(1, 5);
        team.defenders.get(3).homeRegion = Region.getRegion(1, 7);
        
        team.midfielders.get(0).homeRegion = Region.getRegion(2, 1);
        team.midfielders.get(1).homeRegion = Region.getRegion(2, 3);
        team.midfielders.get(2).homeRegion = Region.getRegion(2, 5);
        team.midfielders.get(3).homeRegion = Region.getRegion(2, 7);
        
        team.forwards.get(0).homeRegion = Region.getRegion(3, 2);
        team.forwards.get(1).homeRegion = Region.getRegion(3, 6);
        
        if (team.isOnLeft) return;
        
        // team is on the right side -> mirror image
        for (Player player : team.players) {
            player.homeRegion = Region.getRegion(
                    (Region.NUMBER_OF_COLUMNS - 1) - player.homeRegion.x, player.homeRegion.y);
        }
    }
    
    @Override
    public void enter() {
        setHomeRegions();
        
        Interposes = new ArrayList<Interpose>();
        
        Player controllingOpponent = fsmAI.opponentTeam.getNearestPlayerToBall();
        
        Player firstNearestToControlling = fsmAI.opponentTeam.getNearestPlayerToPosition(
                controllingOpponent.position, controllingOpponent);
        
        Player secondNearestToControlling = fsmAI.opponentTeam.getNearestPlayerToPosition(
                controllingOpponent.position, controllingOpponent, firstNearestToControlling);
        
        Interpose interpose1 = new Interpose(team.forwards.get(0), 2, 0.8, controllingOpponent, firstNearestToControlling);
        Interpose interpose2 = new Interpose(team.forwards.get(1), 2, 0.8, controllingOpponent, secondNearestToControlling);
        
        Interposes.add(interpose1);
        Interposes.add(interpose2);
        
        team.forwards.get(0).steeringBehaviorsManager.addBehavior(interpose1);
        team.forwards.get(1).steeringBehaviorsManager.addBehavior(interpose2);
    }
    
    @Override
    public void run() {
        
        if (team.playerInBallRange != null && fsmAI.opponentTeam.playerInBallRange == null) {
            team.stateMachine.changeState(new Attacking(team, fsmAI));
            return;
        }
        
        updateSteeringBehaviors();
    }
    
    @Override
    public void exit() {
        for (int i = 0; i < 2; i++)
            team.forwards.get(i).steeringBehaviorsManager.removeBehavior(Interposes.get(i));
    }
    
    private void updateSteeringBehaviors() {
        
        Player controllingOpponent = fsmAI.opponentTeam.getNearestPlayerToBall();
        
        Player firstNearestToControlling = fsmAI.opponentTeam.getNearestPlayerToPosition(
                controllingOpponent.position, controllingOpponent);
        
        Player secondNearestToControlling = fsmAI.opponentTeam.getNearestPlayerToPosition(
                controllingOpponent.position, controllingOpponent, firstNearestToControlling);
        
        Interposes.get(0).first = controllingOpponent;
        Interposes.get(1).first = controllingOpponent;
        
        Interposes.get(0).second = firstNearestToControlling;
        Interposes.get(1).second = secondNearestToControlling;
    }
    
}
