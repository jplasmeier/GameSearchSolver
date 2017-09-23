package com.jplaz.eecs391;

import java.util.ArrayList;
import java.util.Random;

public class NPuzzleState implements GameState {

    String gameBoard;

    public NPuzzleState setState(String newState) {
        this.gameBoard = newState;
        return this;
    }

    public NPuzzleState(int size) {
        // for now, ignore size and hardcode it 8

    }

    public NPuzzleState randomizeState(int n) {
        // generate permutation of 1-9
        Random rand = new Random(391L);
        Character possiblePieces[] = {'1', '2', '3', '4', '5', '6', '7', '8', 'b'};
        ArrayList<Character> newStateArrayList = new ArrayList<Character>(9);
        while (newStateArrayList.size() < 9) {
            int randomPiece = rand.nextInt(9);
            if (!newStateArrayList.contains(possiblePieces[randomPiece])) {
                newStateArrayList.add(possiblePieces[randomPiece]);
            }
        }
        StringBuilder newStateString = new StringBuilder(11);
        for (int i = 0; i < newStateArrayList.size(); i++) {
            newStateString.append(newStateArrayList.get(i));
            if ((i+1) % 3 == 0) {
                System.out.println("add a space here");
                newStateString.append(' ');
            }
        }
        this.gameBoard = newStateString.toString();
        return this;
    }

    public NPuzzleState move(String move) {
        return this;
    }

    public boolean isValidMove(String move) {
        return false;
    }

    public NPuzzleState solve(String algorithm, String heuristic) {
        return this;
    }

    public boolean isGoalState() {
        return false;
    }

    public NPuzzleState setMaxNodes(int maxNodes) {
        return this;
    }

    public NPuzzleState printState() {
        System.out.println(gameBoard);
        return this;
    }

    public NPuzzleState applyCommand(Command cmd) {
        return this;
    }

}

