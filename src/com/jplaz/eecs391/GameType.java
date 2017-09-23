package com.jplaz.eecs391;

public enum GameType {
    N_PUZZLE, RUBIKS_CUBE;


    public static GameType stringToGameType(String s) {
        s = s.toLowerCase();
        if (s.equals("8-puzzle")) {
            return N_PUZZLE;
        } else if (s.equals("rubiks-cube")) {
            return RUBIKS_CUBE;
        } else {
            System.out.println("Invalid GameType. Defaulting to 8-puzzle...");
            return N_PUZZLE;
        }
    }
}


