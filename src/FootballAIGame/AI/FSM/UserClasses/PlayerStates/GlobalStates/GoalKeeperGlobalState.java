package FootballAIGame.AI.FSM.UserClasses.PlayerStates.GlobalStates;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.GameClient;
import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;
import FootballAIGame.AI.FSM.UserClasses.Messaging.MessageDispatcher;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.GoDefaultMessage;
import FootballAIGame.AI.FSM.UserClasses.Messaging.Messages.ReceivePassMessage;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.DefendGoal;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.InterceptBall;
import FootballAIGame.AI.FSM.UserClasses.PlayerStates.PlayerState;

public class GoalKeeperGlobalState extends PlayerState {
    
    private PlayerGlobalState playerGlobalState;
    
    public GoalKeeperGlobalState(Player player) {
        super(player);
        playerGlobalState = new PlayerGlobalState(player);
    }
    
    @Override
    public void run() {
        
        if (player.canKickBall(Ai.getInstance().ball)) {
            Player passTargetPlayer;
            if ((passTargetPlayer = Ai.getInstance().myTeam.tryGetSafePass(player)) != null) {
                Vector passTarget = player.passBall(Ai.getInstance().ball, passTargetPlayer);
                MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(passTarget), passTargetPlayer);
            } else {
                // find a safe direction and kick the ball there
                double x = GameClient.FIELD_WIDTH / 2;
                boolean safeDirectionFound = false;
                
                for (int y = 10; y < GameClient.FIELD_HEIGHT; y += 5) {
                    Vector target = new Vector(x, y);
                    if (Ai.getInstance().myTeam.isKickSafe(player, target)) {
                        player.kickBall(Ai.getInstance().ball, target);
                        safeDirectionFound = true;
                        break;
                    }
                }
                
                if (!safeDirectionFound) {
                    // kick randomly
                    Vector target = new Vector(x, Ai.random.nextInt((int) GameClient.FIELD_HEIGHT - 2) + 1);
                    player.kickBall(Ai.getInstance().ball, target);
                }
            }
        }
        playerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        
        if (message instanceof ReceivePassMessage) {
            player.stateMachine.changeState(new InterceptBall(player));
            return true;
        }
        if (message instanceof GoDefaultMessage) {
            player.stateMachine.changeState(new DefendGoal(player));
            return true;
        }
        
        return playerGlobalState.processMessage(message);
    }
}
