package FootballAIGame.AI.FSM.UserClasses.TeamStates;

import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Team;
import FootballAIGame.AI.FSM.UserClasses.State;

public abstract class TeamState extends State<Team> {
    
    protected Team team;
    
    public TeamState(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
        this.team = team;
    }
    
    public abstract void setHomeRegions();
}
