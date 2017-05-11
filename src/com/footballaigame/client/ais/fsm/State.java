package com.footballaigame.client.ais.fsm;

import com.footballaigame.client.ais.fsm.messaging.Message;

/**
 * Provides the base class target which the classes that represent states are derived.
 * @param <TEntity> The type of the entity to which the state belongs.
 */
public abstract class State<TEntity> {
    
    /**
     * The {@link FsmAI} instance to which this instance belongs.
     */
    protected FsmAI fsmAI;
    
    /**
     * The {@link TEntity} to which this instance belongs.
     */
    protected TEntity entity;
    
    /**
     * Initializes a new instance of the {@link State} class.
     * @param entity The entity to which this instance belongs.
     * @param fsmAI The {@link FsmAI} instance to which this player belongs.
     */
    public State(TEntity entity, FsmAI fsmAI) {
        this.entity = entity;
        this.fsmAI = fsmAI;
    }
    
    /**
     * Occurs when the entity enters to this state.
     */
    public void enter() {
    }
    
    /**
     * Occurs every simulation step while the entity is in this state.
     */
    public abstract void run();
    
    /**
     * Occurs when the entity leaves this state.
     */
    public void exit() {
    }
    
    /**
     * Processes the specified message.
     * @param message The message.
     * @return True if the specified message was handled; otherwise, false.
     */
    public boolean processMessage(Message message) {
        return false;
    }
    
}
