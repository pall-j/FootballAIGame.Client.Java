package FootballAIGame.AI.FSM.UserClasses.TeamStates;

import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.State;

public abstract class TeamState extends State<Team> {
    
    protected Team team;
    
    public TeamState(Team team, Ai ai) {
        super(team, ai);
        this.team = team;
    }
    
    public abstract void setHomeRegions();
}
