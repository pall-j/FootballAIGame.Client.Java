package com.footballaigame.client.ais.fsm.playerstates.globalstates;

import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.playerstates.KickBall;
import com.footballaigame.client.ais.fsm.playerstates.PlayerState;
import com.footballaigame.client.ais.fsm.playerstates.PursueBall;

/**
 * Represents the field player's global state. Keeps {@link PlayerGlobalState} internally and calls
 * its methods at the end of its own methods.
 */
public class FieldPlayerGlobalState extends PlayerState {
    
    /**
     * The player's global state that is used at the end of the this state methods.
     */
    private PlayerGlobalState playerGlobalState;
    
    /**
     * Initializes a new instance of the {@link FieldPlayerGlobalState} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public FieldPlayerGlobalState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        playerGlobalState = new PlayerGlobalState(player, fsmAI);
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
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
    
    /**
     * Processes the specified message.
     * @param message The message.
     * @return True if the specified message was handled; otherwise, false.
     */
    @Override
    public boolean processMessage(Message message) {
        return playerGlobalState.processMessage(message);
    }
}
