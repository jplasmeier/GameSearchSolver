package com.jplaz.eecs391;

public enum NPuzzleMove implements Move {
    UP, DOWN, LEFT, RIGHT;

    public static NPuzzleMove stringToMove(String s) throws Exception {
        s = s.toLowerCase();
        switch(s) {
            case "up":
                return NPuzzleMove.UP;
            case "down":
                return NPuzzleMove.DOWN;
            case "left":
                return NPuzzleMove.LEFT;
            case "right":
                return NPuzzleMove.RIGHT;
            default:
                throw new Exception("Invalid movement direction: " + s);
        }
    }
}
