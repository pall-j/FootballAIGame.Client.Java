package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.GameClient;
import FootballAIGame.AI.FSM.UserClasses.FsmAI;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;

public class Dribble extends PlayerState {
    
    public Dribble(Player player, FsmAI fsmAI) {
        super(player, fsmAI);
    }
    
    @Override
    public void enter() {
        fsmAI.myTeam.controllingPlayer = player;
        run(); // run immediately
    }
    
    @Override
    public void run() {
        Vector target = new Vector(90, player.position.y);
        if (!fsmAI.myTeam.isOnLeft)
            target.x = 20;
        
        if (player.position.x > 89 && fsmAI.myTeam.isOnLeft)
            target = new Vector(100, GameClient.FIELD_HEIGHT / 2.0 + (FsmAI.random.nextDouble() - 0.5) * 7.32);
        
        if (player.position.x < 21 && !fsmAI.myTeam.isOnLeft)
            target = new Vector(10, GameClient.FIELD_HEIGHT / 2.0 + (FsmAI.random.nextDouble() - 0.5) * 7.32);
        
        Vector kickDirection = Vector.difference(target, player.position);
        Vector playerFutureMovement = Vector.sum(player.movement, kickDirection.getResized(player.maxAcceleration())).getTruncated(player.maxSpeed());
        double futureSpeedInKickDirection = Vector.dotProduct(playerFutureMovement, kickDirection) / kickDirection.length();
        
        player.kickBall(fsmAI.ball, target, futureSpeedInKickDirection);
        
        player.stateMachine.changeState(new PursueBall(player, fsmAI));
    }
    
}
