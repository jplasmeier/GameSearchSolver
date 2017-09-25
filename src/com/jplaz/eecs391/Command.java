package com.jplaz.eecs391;

import java.util.HashMap;
import java.util.Map;

public enum Command {
    SET_STATE, RANDOMIZE_STATE, PRINT_STATE, MOVE, SOLVE, SET_MAX_NODES;

    public static final Map<String, Command> STRING_COMMAND_MAP;
    // no map literals in Java, so use a static initializer
    // TODO: convert to enum
    static {
        STRING_COMMAND_MAP = new HashMap<>();
        STRING_COMMAND_MAP.put("setState", SET_STATE);
        STRING_COMMAND_MAP.put("randomizeState", RANDOMIZE_STATE);
        STRING_COMMAND_MAP.put("printState", PRINT_STATE);
        STRING_COMMAND_MAP.put("move", MOVE);
        STRING_COMMAND_MAP.put("solve", SOLVE);
        STRING_COMMAND_MAP.put("maxNodes", SET_MAX_NODES);
    }

    public static Command stringToCommand(String s) {
        try {
            return STRING_COMMAND_MAP.get(s);
        } catch (Exception ex) {
            System.out.println("Invalid command issued.");
            throw ex;
        }
    }

}

