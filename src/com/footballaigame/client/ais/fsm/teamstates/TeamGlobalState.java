package com.footballaigame.client.ais.fsm.teamstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Team;

public class TeamGlobalState extends TeamState {
    
    /**
     * Initializes a new instance of the {@link TeamGlobalState} class.
     * @param team The {@link Team} to which this instance belongs.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public TeamGlobalState(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
    }
    
    /**
     * Sets the home regions of the team's players.
     */
    @Override
    public void setHomeRegions() {
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
    @Override
    public void run() {
        
    }
}
