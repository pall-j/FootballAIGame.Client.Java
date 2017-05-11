package com.footballaigame.client.ais.fsm.teamstates;

import com.footballaigame.client.customdatatypes.Region;
import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.SupportControllingMessage;

/**
 * Represents team's attacking state. The team stays in this state while its controlling the ball.
 * When the team looses the ball, the state is changed to {@link Defending}.
 */
public class Attacking extends TeamState {
    
    /**
     * Initializes a new instance of the {@link Attacking} class.
     * @param team The {@link Team} to which this instance belongs.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public Attacking(Team team, FsmAI fsmAI) {
        super(team, fsmAI);
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        setHomeRegions();
    }
    
    /**
     * Sets the home regions of the team's players.
     */
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
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
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
