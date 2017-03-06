package FootballAIGame.AI.FSM.UserClasses.TeamStates;

import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.Default;

import java.util.ArrayList;

public class Kickoff extends TeamState {
    
    public Kickoff(Team team) {
        super(team);
    }
    
    @Override
    public void setHomeRegions() {
    }
    
    @Override
    public void enter() {
        
        team.controllingPlayer = null;
        team.passReceiver = null;
        team.supportingPlayers = new ArrayList<Player>();
        for (Player teamPlayer : team.players) {
            teamPlayer.steeringBehaviorsManager.reset();
            teamPlayer.stateMachine.changeState(new Default(teamPlayer));
        }
        
        
        if (team.playerInBallRange == null) {
            team.stateMachine.changeState(new Defending(team));
        } else {
            team.stateMachine.changeState(new Attacking(team));
        }
    }
    
    @Override
    public void run() {
        
    }
}
