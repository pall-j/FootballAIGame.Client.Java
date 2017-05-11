package com.footballaigame.client.ais.fsm.teamstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.State;

/**
 * Provides the base class from which the classes that represent team's states are derived.
 * Contains the shared functionality of the team's states.
 */
public abstract class TeamState extends State<Team> {
    
    /**
     * The team to which this instance belongs.
     */
    protected Team team;
    
    /**
     * Initializes a new instance of the {@link TeamState} class.
     * @param team The {@link Team} to which this instance belongs.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    protected TeamState(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
        this.team = team;
    }
    
    /**
     * Sets the home regions of the team's players.
     */
    public abstract void setHomeRegions();
}
