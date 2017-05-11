package com.footballaigame.client.ais.fsm.teamstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.playerstates.Default;

import java.util.ArrayList;

/**
 * Represents the team's kickoff state. This is the initial state of the team.
 * The team's state is changed to this when the kickoff is happening.
 * When the team enters this state, all its players' states are changed to {@link Default}.
 * The team's state is changed to {@link Attacking} if the team is doing the kickoff.
 * Otherwise the state is changed to {@link Defending}.
 */
public class Kickoff extends TeamState {
    
    /**
     * Initializes a new instance of the {@link Kickoff} class.
     * @param team The {@link Team} to which this instance belongs.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public Kickoff(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
    }
    
    /**
     * Sets the home regions of the team's players.
     */
    @Override
    public void setHomeRegions() {
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        
        team.controllingPlayer = null;
        team.passReceiver = null;
        team.supportingPlayers = new ArrayList<Player>();
        for (Player teamPlayer : team.players) {
            teamPlayer.steeringBehaviorsManager.reset();
            teamPlayer.stateMachine.changeState(new Default(teamPlayer, fsmAI));
        }
        
        
        if (team.playerInBallRange == null) {
            team.stateMachine.changeState(new Defending(team, fsmAI));
        } else {
            team.stateMachine.changeState(new Attacking(team, fsmAI));
        }
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
    @Override
    public void run() {
        
    }
}
