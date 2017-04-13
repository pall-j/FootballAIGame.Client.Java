package com.footballaigame.client.ais.fsm.playerstates.globalstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.KickBall;
import com.footballaigame.client.ais.fsm.playerstates.PlayerState;
import com.footballaigame.client.ais.fsm.playerstates.PursueBall;

public class FieldPlayerGlobalState extends PlayerState {
    
    private PlayerGlobalState playerGlobalState;
    
    public FieldPlayerGlobalState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        playerGlobalState = new PlayerGlobalState(player, fsmAI);
    }
    
    @Override
    public void run() {
        
        Team team = fsmAI.myTeam;
        
        if (player.canKickBall(fsmAI.ball)) {
            player.stateMachine.changeState(new KickBall(player, fsmAI));
        } else if (team.getNearestPlayerToBall() == player &&
                team.passReceiver == null) {
            player.stateMachine.changeState(new PursueBall(player, fsmAI));
        }
        
        playerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        return playerGlobalState.processMessage(message);
    }
}
