package com.footballaigame.client.ais.fsm.playerstates;

import com.footballaigame.client.customdatatypes.Vector;
import com.footballaigame.client.GameClient;
import com.footballaigame.client.ais.fsm.FsmAI;
import com.footballaigame.client.ais.fsm.entities.Player;

/**
 * Represents the player's dribble state. Player in this state pushes the ball in front of him
 * towards the opponent goal. After each push the state is changed to {@link PursueBall}.
 */
public class Dribble extends PlayerState {
    
    /**
     * Initializes a new instance of the {@link Dribble} class.
     * @param player The player.
     * @param fsmAI The {@link FsmAI} instance to which this instance belongs.
     */
    public Dribble(Player player, FsmAI fsmAI) {
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
        Vector target = new Vector(90, player.position.y);
        if (!fsmAI.myTeam.isOnLeft)
            target.x = 20;
        
        if (player.position.x > 89 && fsmAI.myTeam.isOnLeft)
            target = new Vector(100, GameClient.FIELD_HEIGHT / 2.0 + (FsmAI.random.nextDouble() - 0.5) * 7.32);
        
        if (player.position.x < 21 && !fsmAI.myTeam.isOnLeft)
            target = new Vector(10, GameClient.FIELD_HEIGHT / 2.0 + (FsmAI.random.nextDouble() - 0.5) * 7.32);
        
        Vector kickDirection = Vector.getDifference(target, player.position);
        Vector playerFutureMovement = Vector.getSum(player.movement, kickDirection.getResized(player.getMaxAcceleration())).getTruncated(player.getMaxSpeed());
        double futureSpeedInKickDirection = Vector.getDotProduct(playerFutureMovement, kickDirection) / kickDirection.getLength();
        
        player.kickBall(fsmAI.ball, target, futureSpeedInKickDirection);
        
        player.stateMachine.changeState(new PursueBall(player, fsmAI));
    }
    
}
