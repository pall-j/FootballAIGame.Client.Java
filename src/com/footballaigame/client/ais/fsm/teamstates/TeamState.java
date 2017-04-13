package com.footballaigame.client.ais.fsm.teamstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.State;

public abstract class TeamState extends State<Team> {
    
    protected Team team;
    
    public TeamState(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
        this.team = team;
    }
    
    public abstract void setHomeRegions();
}
