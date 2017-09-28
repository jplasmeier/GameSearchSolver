package com.jplaz.eecs391;

public enum Command {
    SET_STATE, RANDOMIZE_STATE, PRINT_STATE, MOVE, SOLVE, SET_MAX_NODES;

    public static Command stringToCommand(String s) throws Exception {
        switch (s) {
            case "setState":
                return Command.SET_STATE;
            case "randomizeState":
                return Command.RANDOMIZE_STATE;
            case "printState":
                return Command.PRINT_STATE;
            case "move":
                return Command.MOVE;
            case "solve":
                return Command.SOLVE;
            case "maxNodes":
                return SET_MAX_NODES;
            default:
                throw new Exception("Invalid command issued: " + s);
        }
    }

}

