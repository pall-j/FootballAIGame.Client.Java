package com.footballaigame.client.ais.fsm.teamstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.playerstates.Default;

import java.util.ArrayList;

public class Kickoff extends TeamState {
    
    public Kickoff(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
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
            teamPlayer.stateMachine.changeState(new Default(teamPlayer, fsmAI));
        }
        
        
        if (team.playerInBallRange == null) {
            team.stateMachine.changeState(new Defending(team, fsmAI));
        } else {
            team.stateMachine.changeState(new Attacking(team, fsmAI));
        }
    }
    
    @Override
    public void run() {
        
    }
}
