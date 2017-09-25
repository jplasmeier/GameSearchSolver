package com.jplaz.eecs391;

public enum Move {
    UP, DOWN, LEFT, RIGHT;

    public static Move stringToMove(String s) throws Exception {
        s = s.toLowerCase();
        switch(s) {
            case "up":
                return Move.UP;
            case "down":
                return Move.DOWN;
            case "left":
                return Move.LEFT;
            case "right":
                return Move.RIGHT;
            default:
                throw new Exception("Invalid movement direction: " + s);
        }
    }
}
