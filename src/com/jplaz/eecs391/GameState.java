package com.jplaz.eecs391;

interface GameState {

    GameState setState(String newState);

    GameState randomizeState(int n);

    GameState move(String move);

    boolean isValidMove(String move);

    GameState solve(String algorithm, String heuristic);

    boolean isGoalState();

    GameState setMaxNodes(int maxNodes);

    GameState printState();

    GameState applyCommand(Command cmd);
}
