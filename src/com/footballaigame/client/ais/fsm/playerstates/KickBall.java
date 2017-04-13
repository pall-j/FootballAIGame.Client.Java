package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.ReceivePassMessage;

public class KickBall extends PlayerState {
    
    public KickBall(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        fsmAI.myTeam.controllingPlayer = player;
        run(); // run immediately
    }
    
    @Override
    public void run() {
        Team team = fsmAI.myTeam;
        
        if (team.passReceiver != null) {
            team.passReceiver = null;
        }
        
        Vector target;
        if ((target = team.tryGetShotOnGoal(player)) != null) {
            player.kickBall(fsmAI.ball, target);
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        Player passPlayerTarget;
        if (player.isInDanger() && (passPlayerTarget = team.tryGetSafePass(player)) != null) {
            Vector passTarget = player.passBall(fsmAI.ball, passPlayerTarget);
            MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(passTarget), passPlayerTarget);
            player.stateMachine.changeState(new Default(player, fsmAI));
            return;
        }
        
        player.stateMachine.changeState(new Dribble(player, fsmAI));
    }
    
    @Override
    public void exit() {
        if (fsmAI.myTeam.controllingPlayer == player)
            fsmAI.myTeam.controllingPlayer = null;
    }
}
