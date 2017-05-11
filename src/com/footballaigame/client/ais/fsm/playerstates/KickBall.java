package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.entities.Team;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.ReceivePassMessage;

/**
 * Represents the player's kick ball state. Player in this state tries to find the
 * shot on goal. If there is not a safe shot and player is in danger, then he
 * tries to find a safe pass to other team's player. Otherwise
 * the player will go to {@link Dribble} state.
 */
public class KickBall extends PlayerState {
    
    /**
     * Initializes a new instance of the {@link KickBall} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public KickBall(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    @Override
    public void enter() {
        fsmAI.myTeam.controllingPlayer = player;
        run(); // run immediately
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
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
    
    /**
     * Occurs when the entity leaves this state.
     */
    @Override
    public void exit() {
        if (fsmAI.myTeam.controllingPlayer == player)
            fsmAI.myTeam.controllingPlayer = null;
    }
}
