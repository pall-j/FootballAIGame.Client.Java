package FootballAIGame.AI.FSM.UserClasses.SteeringBehaviors;

import FootballAIGame.AI.FSM.CustomDataTypes.Vector;
import FootballAIGame.AI.FSM.UserClasses.Entities.Player;

public abstract class SteeringBehavior {
    
    protected Player player;
    
    public int priority;
    
    public double weight;
    
    public abstract Vector calculateAccelerationVector();
    
    public SteeringBehavior(Player player, int priority, double weight) {
        this.player = player;
        this.priority = priority;
        this.weight = weight;
    }
}
