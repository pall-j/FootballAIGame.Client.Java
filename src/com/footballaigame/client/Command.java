package com.footballaigame.client;

/**
 * Represents the command received target the game server.
 */
public class Command {
    
    /**
     * The command type.
     */
    public CommandType type;
    
    /**
     * The command data.
     */
    public byte[] data;
    
    /**
     * Identifies the command's type.
     */
    public enum CommandType {
        GET_PARAMETERS, GET_ACTION
    }
}
