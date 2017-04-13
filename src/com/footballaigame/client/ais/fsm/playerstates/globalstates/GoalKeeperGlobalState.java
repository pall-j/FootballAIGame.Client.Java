package com.footballaigame.client.ais.fsm.playerstates.globalstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;
import com.footballaigame.client.ais.fsm.messaging.Message;
import com.footballaigame.client.ais.fsm.messaging.MessageDispatcher;
import com.footballaigame.client.ais.fsm.messaging.messages.GoDefaultMessage;
import com.footballaigame.client.ais.fsm.messaging.messages.ReceivePassMessage;
import com.footballaigame.client.ais.fsm.playerstates.DefendGoal;
import com.footballaigame.client.ais.fsm.playerstates.InterceptBall;
import com.footballaigame.client.ais.fsm.playerstates.PlayerState;

public class GoalKeeperGlobalState extends PlayerState {
    
    private PlayerGlobalState playerGlobalState;
    
    public GoalKeeperGlobalState(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
        playerGlobalState = new PlayerGlobalState(player, fsmAI);
    }
    
    @Override
    public void run() {
        
        if (player.canKickBall(fsmAI.ball)) {
            Player passTargetPlayer;
            if ((passTargetPlayer = fsmAI.myTeam.tryGetSafePass(player)) != null) {
                Vector passTarget = player.passBall(fsmAI.ball, passTargetPlayer);
                MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(passTarget), passTargetPlayer);
            } else {
                // find a safe direction and kick the ball there
                double x = GameClient.FIELD_WIDTH / 2;
                boolean safeDirectionFound = false;
                
                for (int y = 10; y < GameClient.FIELD_HEIGHT; y += 5) {
                    Vector target = new Vector(x, y);
                    if (fsmAI.myTeam.isKickSafe(player, target)) {
                        player.kickBall(fsmAI.ball, target);
                        safeDirectionFound = true;
                        break;
                    }
                }
                
                if (!safeDirectionFound) {
                    // kick randomly
                    Vector target = new Vector(x, FsmAI.random.nextInt((int) GameClient.FIELD_HEIGHT - 2) + 1);
                    player.kickBall(fsmAI.ball, target);
                }
            }
        }
        playerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        
        if (message instanceof ReceivePassMessage) {
            player.stateMachine.changeState(new InterceptBall(player, fsmAI));
            return true;
        }
        if (message instanceof GoDefaultMessage) {
            player.stateMachine.changeState(new DefendGoal(player, fsmAI));
            return true;
        }
        
        return playerGlobalState.processMessage(message);
    }
}
