package com.footballaigame.client.ais.fsm;

import com.footballaigame.client.ais.fsm.messaging.Message;

public abstract class State<TEntity> {
    
    protected FsmAI fsmAI;
    
    protected TEntity entity;
    
    public State(TEntity entity, FsmAI fsmAI) {
        this.entity = entity;
        this.fsmAI = fsmAI;
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
