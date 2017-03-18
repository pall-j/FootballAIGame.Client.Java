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
    
    public GoalKeeperGlobalState(Player player, Ai ai) {
        super(player, ai);
        playerGlobalState = new PlayerGlobalState(player, ai);
    }
    
    @Override
    public void run() {
        
        if (player.canKickBall(ai.ball)) {
            Player passTargetPlayer;
            if ((passTargetPlayer = ai.myTeam.tryGetSafePass(player)) != null) {
                Vector passTarget = player.passBall(ai.ball, passTargetPlayer);
                MessageDispatcher.getInstance().sendMessage(new ReceivePassMessage(passTarget), passTargetPlayer);
            } else {
                // find a safe direction and kick the ball there
                double x = GameClient.FIELD_WIDTH / 2;
                boolean safeDirectionFound = false;
                
                for (int y = 10; y < GameClient.FIELD_HEIGHT; y += 5) {
                    Vector target = new Vector(x, y);
                    if (ai.myTeam.isKickSafe(player, target)) {
                        player.kickBall(ai.ball, target);
                        safeDirectionFound = true;
                        break;
                    }
                }
                
                if (!safeDirectionFound) {
                    // kick randomly
                    Vector target = new Vector(x, Ai.random.nextInt((int) GameClient.FIELD_HEIGHT - 2) + 1);
                    player.kickBall(ai.ball, target);
                }
            }
        }
        playerGlobalState.run();
    }
    
    @Override
    public boolean processMessage(Message message) {
        
        if (message instanceof ReceivePassMessage) {
            player.stateMachine.changeState(new InterceptBall(player, ai));
            return true;
        }
        if (message instanceof GoDefaultMessage) {
            player.stateMachine.changeState(new DefendGoal(player, ai));
            return true;
        }
        
        return playerGlobalState.processMessage(message);
    }
}
