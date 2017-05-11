package com.footballaigame.client.ais.fsm;

import com.footballaigame.client.ais.fsm.messaging.Message;

/**
 * Represents the finite state machine.
 * @param <TEntity> The type of the entity to which this state machine belongs.
 */
public class FiniteStateMachine<TEntity> {
    
    /**
     * The current state of the entity.
     */
    public State<TEntity> currentState;
    
    /**
     * The global state of the entity.
     */
    public State<TEntity> globalState;
    
    /**
     * The {@link TEntity} to which this instance belongs.
     */
    public TEntity entity;
    
    /**
     * Initializes a new instance of the {@link FiniteStateMachine} class.
     * @param entity The entity.
     * @param startState The entity's initial state.
     * @param globalState The entity's global state.
     */
    public FiniteStateMachine(TEntity entity, State<TEntity> startState, State<TEntity> globalState) {
        this.entity = entity;
        this.currentState = startState;
        this.globalState = globalState;
    }
    
    /**
     * Changes the entity's current state. Calls the states' enter and exit methods accordingly.
     * @param newState The new state.
     */
    public void changeState(State<TEntity> newState) {
        if (currentState != null)
            currentState.exit();
        
        currentState = newState;
        currentState.enter();
    }
    
    /**
     * Updates the state machine. Should be called every simulation step after the game state is loaded and before the
     * entity's action is retrieved.
     */
    public void update() {
        if (globalState != null)
            globalState.run();
        if (currentState != null)
            currentState.run();
    }
    
    /**
     * Processes the specified message.
     * @param entity The entity.
     * @param message The message.
     */
    public void processMessage(TEntity entity, Message message) {
        if (currentState != null && currentState.processMessage(message))
            return;
        
        if (globalState != null)
            globalState.processMessage(message);
    }
}
