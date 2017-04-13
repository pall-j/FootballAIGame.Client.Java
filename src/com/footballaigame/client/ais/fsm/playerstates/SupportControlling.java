package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.GoalKeeper;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.PassToPlayerMessage;
import com.footballaigame.client.ais.fsm.messaging.messages.SupportControllingMessage;
import com.footballaigame.client.ais.fsm.Parameters;
import com.footballaigame.client.ais.fsm.steeringbehaviors.Arrive;

public class SupportControlling extends PlayerState {
    
    private Arrive arrive;
    
    public SupportControlling(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        arrive = new Arrive(player, 1, 1.0, fsmAI.supportPositionsManager.getBestSupportPosition());
        player.steeringBehaviorsManager.addBehavior(arrive);
        fsmAI.myTeam.supportingPlayers.add(player);
    }
    
    @Override
    public void run() {
        arrive.target = fsmAI.supportPositionsManager.getBestSupportPosition();
        Team team = fsmAI.myTeam;
        
        // nearest except goalkeeper and controlling
        Player nearest = fsmAI.myTeam.getNearestPlayerToPosition(arrive.target, team.goalKeeper, team.controllingPlayer);
        
        // goalkeeper shouldn't go too far from his home region
        if (player instanceof GoalKeeper &&
                Vector.distanceBetween(arrive.target, player.homeRegion.center) > Parameters.MAX_GOALKEEPER_SUPPORTING_DISTANCE) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        // if shot on goal is possible request pass from controlling
        if (fsmAI.myTeam.tryGetShotOnGoal(player) != null && team.controllingPlayer != null)
            MessageDispatcher.getInstance().sendMessage(new PassToPlayerMessage(player));
        
        // someone else is nearer the best position (not goalkeeper)
        if (!(player instanceof GoalKeeper) && nearest != player && nearest != team.controllingPlayer) {
            MessageDispatcher.getInstance().sendMessage(new SupportControllingMessage(), nearest);
            player.stateMachine.changeState(new Default(player, fsmAI));
        }
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(arrive);
        fsmAI.myTeam.supportingPlayers.remove(player);
    }
}
