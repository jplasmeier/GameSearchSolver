package com.jplaz.eecs391;

import java.util.ArrayList;

public class GameSolver {

    private int maxNodes;

    private NPuzzleState currentGameState;

    // use this to avoid reinitializing existing states
    //private ArrayList<short[]> gameStateCache;
    //if (gameStateCache.contains(newBoard)) {
    //    this.currentGameState = currentGameState.setState(gameStateCache.get(gameStateCache.indexOf(newBoard)));
    //}

    public GameSolver(NPuzzleState initialState) {
        this.currentGameState = initialState;
    }

    public void setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
    }

    public int getMaxNodes() {
        return this.maxNodes;
    }

    public void applyCommand(Command cmd, String arg) throws Exception {
        switch (cmd) {
            case SET_STATE:
                this.currentGameState.setState(arg);
                break;
            case RANDOMIZE_STATE:
                this.currentGameState.randomizeState(Integer.parseInt(arg));
                break;
            case PRINT_STATE:
                this.currentGameState.printState();
                break;
            case MOVE:
                this.currentGameState.move(Move.stringToMove(arg));
                break;
            case SOLVE:
                break;
            case SET_MAX_NODES:
                this.maxNodes = Integer.parseInt(arg);
                break;
        }
    }
}
