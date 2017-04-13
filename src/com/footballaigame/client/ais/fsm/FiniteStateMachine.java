package com.footballaigame.client.ais.fsm;

import com.footballaigame.client.ais.fsm.messaging.Message;

public class FiniteStateMachine<TEntity> {
    
    public State<TEntity> currentState;
    
    public State<TEntity> globalState;
    
    public TEntity owner;
    
    public FiniteStateMachine(TEntity owner, State<TEntity> startState, State<TEntity> globalState) {
        this.owner = owner;
        this.currentState = startState;
        this.globalState = globalState;
    }
    
    public void changeState(State<TEntity> newState) {
        if (currentState != null)
            currentState.exit();
        
        currentState = newState;
        currentState.enter();
    }
    
    public void update() {
        if (globalState != null)
            globalState.run();
        if (currentState != null)
            currentState.run();
    }
    
    public void processMessage(TEntity entity, Message message) {
        if (currentState != null && currentState.processMessage(message))
            return;
        
        if (globalState != null)
            globalState.processMessage(message);
    }
}
