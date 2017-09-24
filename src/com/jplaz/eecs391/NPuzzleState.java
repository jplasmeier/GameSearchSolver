package com.jplaz.eecs391;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class NPuzzleState implements GameState {

    private static final char GOAL_STATE[] = {'b','1','2','3','4','5', '6','7','8'};

    private char gameBoard[];

    private int maxNodes;

    public NPuzzleState setState(String newState) {
        char newBoard[] = new char[9];
        int boardPosition = 0;
        for (int i = 0; i < newState.length(); i++) {
            if (newState.charAt(i) != (' ')) {
                newBoard[boardPosition] = newState.charAt(i);
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
        char tmp = gameBoard[a];
        gameBoard[a] = gameBoard[b];
        gameBoard[b] = tmp;
        return this;
    }

    public NPuzzleState move(Move move) {
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
        while (this.gameBoard[blankSpace] != 'b') {
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

    public NPuzzleState solve(String algorithm, String heuristic) {
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

    public NPuzzleState applyCommand(Command cmd) {
        return this;
    }

}

