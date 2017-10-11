package com.jplaz.eecs391;

import java.util.*;

public class GameSolver {

    public class GameComparator implements Comparator<GameState> {

        // the heuristic is used to order the priority queue
        // so its implemented in the comparator and the
        // queue is initialized with this comparator
        private String heuristic;

        public GameComparator(String heuristic) {
            this.heuristic = heuristic;
        }

        public int compare(GameState state1, GameState state2) {
            int f1;
            int f2;
            f1 = state1.getPathCost() + state1.calculateHeuristic(this.heuristic);
            f2 = state2.getPathCost() + state2.calculateHeuristic(this.heuristic);
            return Integer.signum(f1 - f2);
        }
    }

    private int maxNodes;

    private GameState currentGameState;

    public GameSolver(GameState initialState) {
        this.currentGameState = initialState;
    }

    public void setMaxNodes(int maxNodes) {
        System.out.println("Setting maxNodes to: " + maxNodes);
        this.maxNodes = maxNodes;
    }

    public int getMaxNodes() {
        return this.maxNodes;
    }


    public boolean solve(String arg, GameState initialState) {
        // arg is either:
        // "beam k" or "A-star h1" or "A-star h2"
        // so split into tokens and handle accordingly
        String tokens[] = arg.split(" ");
        String algorithm = tokens[0];
        String extraArg = tokens[1];

        boolean success = false;

        if (algorithm.startsWith("beam")) {
            success = beamSearch(initialState, Integer.parseInt(extraArg));
        }
        else if (algorithm.startsWith("A-star")) {
            success = aStarSearch(extraArg, initialState);
        }

        if (success) {
            // solution found, reset to initialState (goal)
            this.currentGameState.setState("b12 345 678");
        }
        else {
            System.out.print("No solution was found, so resetting to previous state: ");
            this.currentGameState.printState();
            this.currentGameState = initialState;
        }
        return success;
    }

    private boolean aStarSearch(String heuristic, GameState initialState) {
        // use this to avoid reinitializing existing states
        // only store the board because the board uniquely identifies objects
        ArrayList<GameState> gameStateCache = new ArrayList<>();
        ArrayList<GameState> staleStates = new ArrayList<>();

        System.out.print("Starting A* search on: ");
        initialState.printState();

        // initialize comparator and priority queue
        GameComparator gameComparator = new GameComparator(heuristic);
        Queue<GameState> queue = new PriorityQueue<>(11, gameComparator);
        int iterations = 0;

        // start with the initialState
        queue.add(initialState);
        initialState.setPathCost(0);
        gameStateCache.add(initialState);

        while (!queue.isEmpty()) {
            // process the next node from the queue
            GameState currentNode = queue.poll();

            // check if it's a goal state
            if (currentNode.isGoalState()) {
                System.out.println("  [SUCCESS] Goal State found in " + iterations + " iterations.");
                currentNode.printPath();
                return true;
            }

            // check maxNodes
            if (iterations >= maxNodes) {
                System.out.println("  [FAILURE] Max nodes reached! Failure!!");
                return false;
            }

            // keep track of previously discovered states and iteration count
            staleStates.add(currentNode);
            iterations++;

            // get the successor states
            ArrayList<Move> validMoves = currentNode.getValidMoves();
            if (validMoves.size() == 0) {
                System.out.println("  [FAILURE] No valid moves left; solution search failed.");
                return false;
            }

            // explore each next state
            for (Move move : validMoves) {
                GameState newState;

                // check cache in case the node was enqueued but not processed
                if (gameStateCache.contains(currentNode.move(move))) {
                    newState = gameStateCache.get(gameStateCache.indexOf(currentNode.move(move)));
                }
                else {
                    newState = currentNode.move(move);
                    newState.setPathToNode(currentNode.getPathToNode());
                    newState.appendMoveToPath(move.toString());
                    gameStateCache.add(newState);
                }

                // if the state has already been processed, skip
                if (staleStates.contains(newState)) {
                    continue;
                }

                // update the path cost, and path
                int newPathCost = currentNode.getPathCost() + 1;
                if (newPathCost < newState.getPathCost()) {
                    newState.setPathCost(newPathCost);
                    newState.setPathToNode(currentNode.getPathToNode());
                    newState.appendMoveToPath(move.toString());
                }

                // finally, enqueue the new state
                if (!queue.contains(newState)) {
                    queue.add(newState);
                }
            }
        }
        return false;
    }

    private boolean beamSearch(GameState initialState, int numberOfStates) {
        System.out.println("Running Beam Search with " + numberOfStates + "states.");
        // initialize beam and gameStateCache
        ArrayList<GameState> beam = new ArrayList<>(numberOfStates);
        ArrayList<GameState> gameStateCache = new ArrayList<>();
        GameComparator gameComparator = new GameComparator("h2");
        PriorityQueue<GameState> beamCache = new PriorityQueue<>(gameComparator);
        int iterations = 0;

        beam.add(initialState);

        while (!beam.isEmpty()) {
            beamCache.clear();
            // for each state in the beam, generate k random states
            for (GameState beamState : beam) {
                for (Move move : beamState.getValidMoves()) {
                    GameState newState = beamState.move(move);
                    newState.setPathToNode(beamState.getPathToNode());
                    newState.appendMoveToPath(move.toString());

                    // check for goal state
                    if (newState.isGoalState()) {
                        System.out.println("  [SUCCESS] Goal state found in " + (iterations + 1) + " iterations!");
                        newState.printPath();
                        return true;
                    }

                    // check maxNodes
                    if (iterations >= maxNodes) {
                        System.out.println("  [FAILURE] Max nodes exceeded! Failing...");
                        return false;
                    }

                    // add to beam cache
                    beamCache.add(newState);
                }
            }

            // clear out the beam
            beam.clear();
            iterations++;

            // select k best (using heuristic) states to keep from the beam cache
            while (!beamCache.isEmpty() && beam.size() < numberOfStates) {
                GameState currentState = beamCache.poll();
                if (!gameStateCache.contains(currentState)) {
                    gameStateCache.add(currentState);
                    beam.add(currentState);
                }
            }
        }
        System.out.println("Goal state was not found after " + iterations + " iterations.");
        return false;
    }

    public boolean applyCommand(Command cmd, String arg) throws Exception {
        switch (cmd) {
            case SET_STATE:
                this.currentGameState.setState(arg);
                break;
            case RANDOMIZE_STATE:
                this.currentGameState = currentGameState.randomizeState(Integer.parseInt(arg));
                break;
            case PRINT_STATE:
                this.currentGameState.printState();
                break;
            case MOVE:
                this.currentGameState.move(NPuzzleMove.stringToMove(arg));
                break;
            case SOLVE:
                return this.solve(arg, currentGameState);
            case SET_MAX_NODES:
                this.setMaxNodes(Integer.parseInt(arg));
                break;
            case NOOP:
                // do...nothing!
                System.out.println("I'm not familiar with this command. Please try again.");
                break;
        }
        // really this shouldn't return anything
        // but it will return true upon successful solution
        // kind of a hack but oh well
        return false;
    }
}
