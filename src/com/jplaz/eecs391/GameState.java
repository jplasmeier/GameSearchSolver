package com.jplaz.eecs391;

import java.util.ArrayList;
import java.util.LinkedList;

interface GameState {

    // getters & setters

    int getPathCost();

    void setPathCost(int pathCost);

    LinkedList getPathToNode();

    void setPathToNode(LinkedList newPath);

    void setState(String state);

    // state manipulation

    void appendMoveToPath(Move move);

    GameState randomizeState(int n);

    GameState move(Move move);

    // solver methods

    int calculateHeuristic(String heuristic);

    ArrayList<Move> getValidMoves();

    boolean isGoalState();

    // print methods

    void printState();

    void printPath();

}
