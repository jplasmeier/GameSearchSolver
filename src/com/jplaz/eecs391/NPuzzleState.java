package com.jplaz.eecs391;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NPuzzleState implements GameState {

    private static final short GOAL_STATE[] = {0,1,2,3,4,5,6,7,8};

    private short gameBoard[];

    private int maxNodes;

    public NPuzzleState setState(String newState) {
        short newBoard[] = new short[9];
        int boardPosition = 0;
        for (int i = 0; i < newState.length(); i++) {
            // skip the quotation marks and spaces
            if (!(newState.charAt(i) == '\"' || newState.charAt(i) == (' '))) {
                // b is not a number; 0 is preferred for calculations
                if (newState.charAt(i) == 'b') {
                    newBoard[boardPosition] = 0;
                }
                else {
                    newBoard[boardPosition] = (short) Character.getNumericValue(newState.charAt(i));
                }
                boardPosition++;
            }
        }
        this.gameBoard = newBoard;
        return this;
    }

    public NPuzzleState randomizeState(int n) {
        Random rand = new Random(391L);
        this.gameBoard = GOAL_STATE;

        for (int i = 0; i < n; i++) {
            this.printState();
            makeRandomMove(rand);
        }
        return this;
    }

    private NPuzzleState makeRandomMove(Random rand) {
        ArrayList<Move> validMoves = this.getValidMoves();
        Move randomMove = validMoves.get(rand.nextInt(validMoves.size()));
        move(randomMove);
        return this;
    }

    private NPuzzleState swap(int a, int b) {
        short tmp = gameBoard[a];
        gameBoard[a] = gameBoard[b];
        gameBoard[b] = tmp;
        return this;
    }

    public NPuzzleState move(Move move) {
        if (!isValidMove(move)) {
            System.out.println("Invalid Move! Try again.");
            return this;
        }
        int blankSpace = findBlankSpace();
        switch (move) {
            case UP:
                return swap(blankSpace, blankSpace - 3);
            case DOWN:
                return swap(blankSpace, blankSpace + 3);
            case LEFT:
                return swap(blankSpace, blankSpace - 1);
            case RIGHT:
                return swap(blankSpace, blankSpace + 1);
        }
        return this;
    }

    public ArrayList<Move> getValidMoves() {
        int numberOfMoves = Move.values().length;
        ArrayList<Move> validMoves = new ArrayList<>(numberOfMoves);

        // check the validity of each move
        for (Move move : Move.values()) {
            if (isValidMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private int findBlankSpace() {
        int blankSpace = 0;
        while (this.gameBoard[blankSpace] != 0) {
            blankSpace++;
        }
        return blankSpace;
    }

    public boolean isValidMove(Move move) {
        int blankSpace = findBlankSpace();
        switch (move) {
            case UP:
                return (3 <= blankSpace && blankSpace <= 8);
            case DOWN:
                return (blankSpace <= 5);
            case LEFT:
                return (blankSpace % 3 != 0);
            case RIGHT:
                return ((blankSpace + 1) % 3 != 0);
        }
        return false;
    }

    public NPuzzleState solve(String algorithm) {
        // algorithm is either
        // "beam" or "A-star h1" or "A-star h2"
        if (algorithm.startsWith("beam")) {
            return beamSearch();
        }
        else if (algorithm.startsWith("A-star")) {
            String heuristic = algorithm.split(" ")[1];
            return aStarSearch(heuristic);
        }
        return this;
    }

    /*
     * Heuristic h1
     */
    private int numberOfMisplacedTiles() {
        int numberOfTiles = 0;
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] != GOAL_STATE[i]) {
                numberOfTiles++;
            }
        }
        return numberOfTiles;
    }

    /*
     * Heuristic h2
     */
    private int boardManhattanDistance() {
        int distance = 0;
        for (int i = 0; i < gameBoard.length; i++) {
            distance += tileManhattanDistance(i);
        }
        return distance;
    }

    /*
     * Calculates the Manhattan Distance of a given board position.
     */
    private int tileManhattanDistance(int index) {
        // each move is either -3, -1, +1, +3
        int difference = Math.abs(index-gameBoard[index]);
        switch (difference) {
            case (0):
                return 0;
            case (1):
                return 1;
            case (2):
                return 2;
            case (3):
                return 1;
            case (4):
                return 2;
            case (5):
                return 3;
            case (6):
                return 2;
            case (7):
                return 3;
            case (8):
                return 4;
            default:
                return 0;
        }

    }

    private NPuzzleState aStarSearch(String heuristic) {
        // if this is a goal state, return
        if (isGoalState()) {
            return this;
        }

        ArrayList<Move> validMoves = getValidMoves();
        if (validMoves.size() == 0) {
            System.out.println("No valid moves left; solution search failed.");
            return this;
        }
        
        switch (heuristic) {
            case ("h1"):
                System.out.println("misplaced tiles: " + numberOfMisplacedTiles());
                return this;
            case ("h2"):
                System.out.println("Using Manhattan Distance: " + boardManhattanDistance());
                return this;
        }
        return this;
    }

    private NPuzzleState beamSearch() {
        return this;
    }

    public boolean isGoalState() {
        return Arrays.equals(this.gameBoard, GOAL_STATE);
    }

    public NPuzzleState setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
        return this;
    }

    public int getMaxNodes() {
        return this.maxNodes;
    }

    public NPuzzleState printState() {
        for (int i = 0; i < gameBoard.length; i++) {
            if (i % 3 == 0) {
                System.out.println();
            }
            System.out.print(gameBoard[i]);
        }
        System.out.println();
        return this;
    }

    public NPuzzleState applyCommand(Command cmd, String arg) throws Exception {
        switch (cmd) {
            case SET_STATE:
                return this.setState(arg);
            case RANDOMIZE_STATE:
                return this.randomizeState(Integer.parseInt(arg));
            case PRINT_STATE:
                return this.printState();
            case MOVE:
                return this.move(Move.stringToMove(arg));
            case SOLVE:
                return this.solve(arg);
            case SET_MAX_NODES:
                return this.setMaxNodes(Integer.parseInt(arg));
            default:
                return this;
        }
    }

}

