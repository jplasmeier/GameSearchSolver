package com.jplaz.eecs391;

import java.util.ArrayList;

interface GameState {

    GameState setState(String newState);

    GameState randomizeState(int n);

    GameState move(Move move);

    boolean isValidMove(Move move);

    // useful for randomization
    ArrayList<Move> getValidMoves();

    GameState solve(String algorithm);

    boolean isGoalState();

    GameState setMaxNodes(int maxNodes);

    GameState printState();

    GameState applyCommand(Command cmd, String args) throws Exception;
}
