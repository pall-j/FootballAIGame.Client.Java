package FootballAIGame.AI.FSM.UserClasses.PlayerStates;

import FootballAIGame.AI.FSM.UserClasses.Ai;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;
import FootballAIGame.AI.FSM.UserClasses.State;

public abstract class PlayerState extends State<Player> {
    
    protected Player player;
    
    public PlayerState(Player player, Ai ai) {
        super(player, ai);
        this.player = player;
    }
}
