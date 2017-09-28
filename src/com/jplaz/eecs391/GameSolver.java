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

    // use this to avoid reinitializing existing states
    // only store the board because the board uniquely identifies objects
    private HashMap<short[], NPuzzleState> gameStateCache = new HashMap<>();
    //if (gameStateCache.contains(newBoard)) {
    //    this.currentGameState = currentGameState.setState(gameStateCache.get(gameStateCache.indexOf(newBoard)));
    //}

    private ArrayList<NPuzzleState> staleStates = new ArrayList<>();

    public GameSolver(NPuzzleState initialState) {
        this.currentGameState = initialState;
    }

    public void setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
    }

    public int getMaxNodes() {
        return this.maxNodes;
    }


    public void solve(String algorithm, NPuzzleState initialState) {
        // algorithm is either:
        // "beam" or "A-star h1" or "A-star h2"
        if (algorithm.startsWith("beam")) {
            beamSearch(initialState);
        }
        else if (algorithm.startsWith("A-star")) {
            String heuristic = algorithm.split(" ")[1];
            aStarSearch(heuristic, initialState);
        }
    }

    private void aStarSearch(String heuristic, NPuzzleState initialState) {
        System.out.print("Starting A* search on: ");
        initialState.printState();

        GameComparator gameComparator = new GameComparator(heuristic);
        Queue<NPuzzleState> queue = new PriorityQueue<>(11, gameComparator);
        queue.add(initialState);
        initialState.setPathCost(0);
        int iterations = 0;

        gameStateCache.put(initialState.getState(), initialState);

        while (!queue.isEmpty()) {
            // grab the next node from the queue
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

            // keep track of previously discovered states
            staleStates.add(currentNode);

            // get the successor states
            ArrayList<Move> validMoves = currentNode.getValidMoves();
            if (validMoves.size() == 0) {
                System.out.println("No valid moves left; solution search failed.");
                return;
            }

            iterations++;
            for (Move move : validMoves) {
                NPuzzleState newState;
                // could check cache
                // in case the node was enqueued but not processed
                if (gameStateCache.containsKey(currentNode.move(move))) {
                    newState = gameStateCache.get(currentNode.move(move));
                }
                else {
                    newState = new NPuzzleState(currentNode.move(move));
                    gameStateCache.put(newState.getState(), newState);
                }

                if (staleStates.contains(newState)) {
                    continue;
                }

                int newG = currentNode.getPathCost() + 1;
                if (newG < newState.getPathCost()) {
                    newState.setPathCost(newG);
                }

                if (!queue.contains(newState)) {
                    queue.add(newState);
                }
            }
        }
    }

    private void beamSearch(NPuzzleState initialState) {
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
