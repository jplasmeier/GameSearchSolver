package com.jplaz.eecs391;

import java.util.*;

public class GameSolver {

    public class GameComparator implements Comparator<NPuzzleState> {

        // the heuristic is used to order the priority queue
        // so its implemented in the comparator and the
        // queue is initialized with this comparator
        private String heuristic;

        public GameComparator(String heuristic) {
            this.heuristic = heuristic;
        }

        public int compare(NPuzzleState state1, NPuzzleState state2) {
            int f1;
            int f2;
            switch (this.heuristic) {
                case "h1":
                    f1 = state1.getPathCost() + state1.numberOfMisplacedTiles();
                    f2 = state2.getPathCost() + state2.numberOfMisplacedTiles();
                    return Integer.signum(f1 - f2);
                case "h2":
                    f1 = state1.getPathCost() + state1.boardManhattanDistance();
                    f2 = state2.getPathCost() + state2.boardManhattanDistance();
                    return Integer.signum(f1 - f2);
                default:
                    // no heuristic -> no ordering
                    return 0;
            }
        }
    }

    private int maxNodes;

    private NPuzzleState currentGameState;

    public GameSolver(NPuzzleState initialState) {
        this.currentGameState = initialState;
    }

    public void setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
    }

    public int getMaxNodes() {
        return this.maxNodes;
    }


    public void solve(String arg, NPuzzleState initialState) {
        // arg is either:
        // "beam k" or "A-star h1" or "A-star h2"
        // so split into tokens and handle accordingly
        String tokens[] = arg.split(" ");
        String algorithm = tokens[0];
        String extraArg = tokens[1];

        if (algorithm.startsWith("beam")) {
            beamSearch(initialState, Integer.parseInt(extraArg));
        }
        else if (algorithm.startsWith("A-star")) {
            aStarSearch(extraArg, initialState);
        }
    }

    private void aStarSearch(String heuristic, NPuzzleState initialState) {
        // use this to avoid reinitializing existing states
        // only store the board because the board uniquely identifies objects
        HashMap<short[], NPuzzleState> gameStateCache = new HashMap<>();
        ArrayList<NPuzzleState> staleStates = new ArrayList<>();

        System.out.print("Starting A* search on: ");
        initialState.printState();

        // initialize comparator and priority queue
        GameComparator gameComparator = new GameComparator(heuristic);
        Queue<NPuzzleState> queue = new PriorityQueue<>(11, gameComparator);
        int iterations = 0;

        // start with the initialState
        queue.add(initialState);
        initialState.setPathCost(0);
        gameStateCache.put(initialState.getState(), initialState);

        while (!queue.isEmpty()) {
            // process the next node from the queue
            NPuzzleState currentNode = queue.poll();
            System.out.print("Processing: ") ;
            currentNode.printState();
            System.out.println("The queue now is size: " + queue.size());

            // check if it's a goal state
            if (currentNode.isGoalState()) {
                System.out.print("Goal State found in " + iterations + " iterations.");
                currentNode.printState();
                return;
            }

            // keep track of previously discovered states and iteration count
            staleStates.add(currentNode);
            iterations++;

            // get the successor states
            ArrayList<Move> validMoves = currentNode.getValidMoves();
            if (validMoves.size() == 0) {
                System.out.println("No valid moves left; solution search failed.");
                return;
            }

            // explore each next state
            for (Move move : validMoves) {
                NPuzzleState newState;

                // check cache in case the node was enqueued but not processed
                if (gameStateCache.containsKey(currentNode.move(move))) {
                    newState = gameStateCache.get(currentNode.move(move));
                }
                else {
                    newState = new NPuzzleState(currentNode.move(move));
                    gameStateCache.put(newState.getState(), newState);
                }

                // if the state has already been processed, skip
                if (staleStates.contains(newState)) {
                    continue;
                }

                // update the path cost
                int newPathCost = currentNode.getPathCost() + 1;
                if (newPathCost < newState.getPathCost()) {
                    newState.setPathCost(newPathCost);
                }

                // finally, enqueue the new state
                if (!queue.contains(newState)) {
                    queue.add(newState);
                }
            }
        }
    }

    private void beamSearch(NPuzzleState initialState, int numberOfStates) {
        System.out.println("Running Beam Search with " + numberOfStates + "states.");
        // initialize beam and gameStateCache
        ArrayList<NPuzzleState> beam = new ArrayList<>(numberOfStates);
        ArrayList<NPuzzleState> gameStateCache = new ArrayList<>();
        GameComparator gameComparator = new GameComparator("h2");
        PriorityQueue<NPuzzleState> beamCache = new PriorityQueue<>(gameComparator);
        int iterations = 0;

        beam.add(initialState);

        while (!beam.isEmpty()) {
            beamCache.clear();
            // for each state in the beam, generate k random states
            for (NPuzzleState beamState : beam) {
                System.out.print("Searching from state: ");
                beamState.printState();
                for (Move move : beamState.getValidMoves()) {
                    NPuzzleState newState = new NPuzzleState(beamState.move(move));

                    // check for goal state
                    if (newState.isGoalState()) {
                        System.out.println("Goal state found in " + (iterations + 1) + " iterations!");
                        newState.printState();
                        return;
                    }
                    // add to beam cache
                    beamCache.add(newState);
                }
            }

            // clear out the beam
            beam.clear();
            iterations++;

            // select k best (using heuristic?) states to keep from the beam cache
            while (!beamCache.isEmpty() && beam.size() < numberOfStates) {
                NPuzzleState currentState = beamCache.poll();
                if (!gameStateCache.contains(currentState)) {
                    System.out.print("Adding to the cache and queue: ");
                    currentState.printState();
                    gameStateCache.add(currentState);
                    beam.add(currentState);
                }
            }
        }
        System.out.println("Goal state was not found after " + iterations + " iterations.");
    }

    public void applyCommand(Command cmd, String arg) throws Exception {
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
                this.currentGameState.move(Move.stringToMove(arg));
                break;
            case SOLVE:
                this.solve(arg, currentGameState);
                break;
            case SET_MAX_NODES:
                this.maxNodes = Integer.parseInt(arg);
                break;
        }
    }
}
