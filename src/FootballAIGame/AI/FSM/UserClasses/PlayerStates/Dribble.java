package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.GameClient;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;

public class Dribble extends PlayerState {
    
    public Dribble(Player player) {
        super(player);
    }
    
    @Override
    public void enter() {
        Ai.getInstance().myTeam.controllingPlayer = player;
        run(); // run immediately
    }
    
    @Override
    public void run() {
        Vector target = new Vector(90, player.position.y);
        if (!Ai.getInstance().myTeam.isOnLeft)
            target.x = 20;
        
        if (player.position.x > 89 && Ai.getInstance().myTeam.isOnLeft)
            target = new Vector(100, GameClient.FIELD_HEIGHT / 2.0 + (Ai.random.nextDouble() - 0.5) * 7.32);
        
        if (player.position.x < 21 && !Ai.getInstance().myTeam.isOnLeft)
            target = new Vector(10, GameClient.FIELD_HEIGHT / 2.0 + (Ai.random.nextDouble() - 0.5) * 7.32);
        
        Vector kickDirection = Vector.difference(target, player.position);
        Vector playerFutureMovement = Vector.sum(player.movement, kickDirection.resized(player.maxAcceleration())).truncated(player.maxSpeed());
        double futureSpeedInKickDirection = Vector.dotProduct(playerFutureMovement, kickDirection) / kickDirection.length();
        
        player.kickBall(Ai.getInstance().ball, target, futureSpeedInKickDirection);
        
        player.stateMachine.changeState(new PursueBall(player));
    }
    
}
