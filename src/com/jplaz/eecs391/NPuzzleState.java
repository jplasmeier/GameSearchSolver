package com.jplaz.eecs391;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class NPuzzleState {

    private static final short GOAL_STATE[] = {0,1,2,3,4,5,6,7,8};

    private short gameBoard[];

    // initialize to max int so the initial cost comparison is always chosen
    private int pathCost = Integer.MAX_VALUE;

    // store the path to this node as a LinkedList of moves
    private LinkedList<Move> pathToNode = new LinkedList<>();

    public NPuzzleState(short[] initialBoard) {
        this.gameBoard = new short[GOAL_STATE.length];
        System.arraycopy(initialBoard, 0, this.gameBoard, 0, initialBoard.length);
    }

    public NPuzzleState(String initialState) {
        setState(initialState);
    }

    public short[] getState() {
        return this.gameBoard;
    }

    public void setState(short[] newBoard) {
        System.arraycopy(newBoard, 0, this.gameBoard, 0, newBoard.length);
    }

    public void setState(String newState) {
        short newBoard[] = new short[9];
        int boardPosition = 0;
        for (int i = 0; i < newState.length(); i++) {
            // skip the quotation marks and spaces
            if (!(newState.charAt(i) == '\"' || newState.charAt(i) == (' '))) {
                // b is not a number; 0 is preferred for calculations
                // see "Representing the GameState" for more info
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
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public LinkedList getPathToNode() {
        return this.pathToNode;
    }

    public void setPathToNode(LinkedList newPath) {
        this.pathToNode = new LinkedList<>(newPath);
    }

    public void appendMoveToPath(Move move) {
        this.pathToNode.add(move);
    }

    // Public Methods

    public void printState() {
        for (int i = 0; i < gameBoard.length; i++) {
            if (i % 3 == 0) {
                System.out.print(" ");
            }
            System.out.print(gameBoard[i]);
        }
        System.out.println();
    }

    public void printPath() {
        for (Move move : this.pathToNode) {
            System.out.print(move.toString() + " ");
        }
    }

    public boolean isGoalState() {
        for (int i = 0; i < GOAL_STATE.length; i++) {
            if (GOAL_STATE[i] != this.gameBoard[i]) {
                return false;
            }
        }
        return true;
    }

    public NPuzzleState randomizeState(int n) {
        Random rand = new Random(391L);
        System.arraycopy(GOAL_STATE, 0, this.gameBoard, 0, GOAL_STATE.length);

        for (int i = 0; i < n; i++) {
            gameBoard = makeRandomMove(rand);
        }
        return this;
    }

    public short[] move(Move move) {
        if (!isValidMove(move)) {
            System.out.println("Invalid Move! Try again.");
            return gameBoard;
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
        return gameBoard;
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

    // Movement Helpers

    private short[] makeRandomMove(Random rand) {
        ArrayList<Move> validMoves = this.getValidMoves();
        Move randomMove = validMoves.get(rand.nextInt(validMoves.size()));
        return move(randomMove);
    }

    private short[] swap(int a, int b) {
        // make a deep copy of the array and return it
        // this retains the property that each NPuzzleState
        // object is a unique board state
        short newBoard[] = new short[gameBoard.length];
        System.arraycopy(gameBoard, 0, newBoard, 0, gameBoard.length);
        short tmp = gameBoard[a];
        newBoard[a] = gameBoard[b];
        newBoard[b] = tmp;
        return newBoard;
    }

    private int findBlankSpace() {
        int blankSpace = 0;
        while (this.gameBoard[blankSpace] != 0) {
            blankSpace++;
        }
        return blankSpace;
    }

    private boolean isValidMove(Move move) {
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

    // Heuristics

    /*
     * Heuristic h1
     */
    public int numberOfMisplacedTiles() {
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
    public int boardManhattanDistance() {
        int distance = 0;
        for (int i = 0; i < gameBoard.length; i++) {
            distance += tileManhattanDistance(gameBoard, i);
        }
        return distance;
    }

    /*
     * Calculates the Manhattan Distance of a given board position.
     */
    private int tileManhattanDistance(short[] board, int index) {
        // each move is either -3, -1, +1, +3
        // the number of +1/+3 operations required to equal
        // the difference is the manhattan distance
        // e.g. for difference = 7: 3 + 3 + 1 -> 3 moves
        int difference = Math.abs(index-board[index]);
        // this doesn't have to be hardcoded, but it is for now
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

    // Overridden methods

    @Override
    public boolean equals(Object o2) {
        if (!(o2 instanceof NPuzzleState)) {
            return false;
        }
        NPuzzleState n2 = (NPuzzleState) o2;

        for (int i = 0; i < this.gameBoard.length; i++) {
            if (this.gameBoard[i] != n2.gameBoard[i]) {
                return false;
            }
        }
        return true;

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.gameBoard);
    }
}

