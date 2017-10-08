package com.jplaz.eecs391;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class RubiksPuzzleState implements GameState {

    private final static int BOARD_SIZE = 2*2*6;

    private final static short GOAL_STATE[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};

    private short gameBoard[];

    // initialize to max int so the initial cost comparison is always chosen
    private int pathCost = Integer.MAX_VALUE;

    private LinkedList<String> pathToNode = new LinkedList<>();

    public RubiksPuzzleState(short[] initialBoard) {
        this.gameBoard = new short[GOAL_STATE.length];
        System.arraycopy(initialBoard, 0, this.gameBoard, 0, initialBoard.length);
    }

    public int getPathCost() {
        return this.pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public LinkedList getPathToNode() {
        return this.pathToNode;
    }

    public void setPathToNode(LinkedList newPath) {
        this.pathToNode = newPath;
    }
    public void setState(String state) {
        // pending definition of String representation
    }

    public void appendMoveToPath(String moveString) {
        this.pathToNode.add(moveString);
    }

    public RubiksPuzzleState randomizeState(int n) {
        return null;
    }

    public RubiksPuzzleState move(Move move, Side side) {
        if (!isValidMove(move, side)) {
            System.out.println("Invalid Move! Try again.");
            return this;
        }
        short newBoard[] = new short[BOARD_SIZE];
        System.arraycopy(this.gameBoard, 0, newBoard, 0, BOARD_SIZE);

        // in theory this can be generalized
        // I just have no idea how to do that
        // the ordering is clockwise
        // so CW move has 12 replace 13 replace 14 replace 15 replace 12
        switch (side) {
            case FRONT:
                newBoard = shiftPositionsClockwise(newBoard, move, 12, 13, 14, 15);
                newBoard = shiftPositionsClockwise(newBoard, move, 3, 11, 19, 23);
                newBoard = shiftPositionsClockwise(newBoard, move, 2, 10, 18, 22);
                return new RubiksPuzzleState(newBoard);
            case BACK:
                newBoard = shiftPositionsClockwise(newBoard, move, 4, 5, 6, 7);
                newBoard = shiftPositionsClockwise(newBoard, move, 1, 21, 17, 9);
                newBoard = shiftPositionsClockwise(newBoard, move, 0, 20, 16, 8);
                return new RubiksPuzzleState(newBoard);
            case LEFT:
                newBoard = shiftPositionsClockwise(newBoard, move, 0, 1, 2, 3);
                newBoard = shiftPositionsClockwise(newBoard, move, 4, 8, 12, 22);
                newBoard = shiftPositionsClockwise(newBoard, move, 7, 11, 15, 21);
                return new RubiksPuzzleState(newBoard);
            case RIGHT:
                newBoard = shiftPositionsClockwise(newBoard, move, 16, 17, 18, 19);
                newBoard = shiftPositionsClockwise(newBoard, move, 6, 20, 14, 10);
                newBoard = shiftPositionsClockwise(newBoard, move, 5, 23, 13, 9);
                return new RubiksPuzzleState(newBoard);
            case TOP:
                newBoard = shiftPositionsClockwise(newBoard, move, 8, 9, 10, 11);
                newBoard = shiftPositionsClockwise(newBoard, move, 2, 7, 13, 16);
                newBoard = shiftPositionsClockwise(newBoard, move, 1, 6, 19, 12);
                return new RubiksPuzzleState(newBoard);
            case BOTTOM:
                newBoard = shiftPositionsClockwise(newBoard, move, 20, 21, 22, 23);
                newBoard = shiftPositionsClockwise(newBoard, move, 3, 14, 17, 4);
                newBoard = shiftPositionsClockwise(newBoard, move, 0, 15, 18, 5);
                return new RubiksPuzzleState(newBoard);
            default:
                return this;
        }
    }

    /*
     * move the given positions, expecting a clockwise ordering
     */
    private short[] shiftPositionsClockwise(short[] gameBoard, Move direction, int a, int b, int c, int d) {
        short tmp = gameBoard[a];
        switch (direction) {
            case CLOCKWISE:
                gameBoard[a] = gameBoard[d];
                gameBoard[d] = gameBoard[c];
                gameBoard[c] = gameBoard[b];
                gameBoard[b] = tmp;
                return gameBoard;
            case COUNTERCLOCKWISE:
                gameBoard[a] = gameBoard[b];
                gameBoard[b] = gameBoard[c];
                gameBoard[c] = gameBoard[d];
                gameBoard[d] = tmp;
                return gameBoard;
            default:
                System.out.println("Invalid Move direction.");
                return gameBoard;
        }
    }

    public int calculateHeuristic(String heuristic) {
        return 0;
    }

    public ArrayList<Move> getValidMoves() {
        // unlike the 8-puzzle, all moves are valid
        return new ArrayList<>(Arrays.asList(Move.values()));
    }

    private boolean isValidMove(Move move, Side side) {
        // unlike the 8-puzzle, all moves are valid
        return true;
    }

    public boolean isGoalState() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (GOAL_STATE[i] != this.gameBoard[i]) {
                return false;
            }
        }
        return true;
    }

    public void printState() {

    }

    public void printPath() {

    }
}
