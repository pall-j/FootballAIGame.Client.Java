package FootballAIGame.AI.FSM.UserClasses;

import FootballAIGame.AI.FSM.UserClasses.Messaging.Message;

public abstract class State<TEntity> {
    
    protected Ai ai;
    
    protected TEntity entity;
    
    public State(TEntity entity, Ai ai) {
        this.entity = entity;
        this.ai = ai;
    }
    
    public void enter() {
    }
    
    public abstract void run();
    
    public void exit() {
    }
    
    public boolean processMessage(Message message) {
        return false;
    }
    
}
