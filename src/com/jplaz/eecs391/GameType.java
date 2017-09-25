package com.jplaz.eecs391;

public enum GameType {
    N_PUZZLE, RUBIKS_CUBE;

    public static GameType stringToGameType(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "8-puzzle":
                return N_PUZZLE;
            case "rubiks-cube":
                return RUBIKS_CUBE;
            default:
                System.out.println("Invalid GameType. Defaulting to 8-puzzle...");
                return N_PUZZLE;
        }
    }
}


