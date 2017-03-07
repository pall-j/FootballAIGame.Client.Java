package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors.Arrive;

public class MoveToHomeRegion extends PlayerState {
    
    private Arrive moveToHomeRegionArrive;
    
    public MoveToHomeRegion(Player player) {
        super(player);
    }
    
    @Override
    public void enter() {
        moveToHomeRegionArrive = new Arrive(player, 3, 1, player.homeRegion.center);
        player.steeringBehaviorsManager.addBehavior(moveToHomeRegionArrive);
    }
    
    @Override
    public void run() {
        moveToHomeRegionArrive.target = player.homeRegion.center;
        if (player.isAtHomeRegion() && Math.abs(player.currentSpeed()) < 0.00001)
            player.stateMachine.changeState(new Default(player));
    }
    
    @Override
    public void exit() {
        player.steeringBehaviorsManager.removeBehavior(moveToHomeRegionArrive);
    }
}
