package com.jplaz.eecs391;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static void loopMode(GameType gameType, int gameSize) throws Exception {

        System.out.println("Welcome to GameSearchSolver. Please enter a command.");
        Scanner inputScanner = new Scanner(System.in);
        NPuzzleState puzzleState = new NPuzzleState();
        puzzleState.setState("b12 345 678");
        while(true) {
            String input_line = inputScanner.nextLine();
            String input_tokens[] = input_line.split(" ", 2);
            String input_command = input_tokens[0];
            String input_arguments = "";
            if (!input_command.equals("printState")) {
                input_arguments = input_tokens[1];
            }
            Command command = Command.stringToCommand(input_command);
            puzzleState.applyCommand(command, input_arguments);
        }
    }

    private static void fileMode(String filename, GameType gameType, int gameSize) {
        System.out.println("File mode selected. Opening " + filename);
    }

    public static void main(String[] args) throws Exception {
        // initialize to defaults
        Mode mode = Mode.LOOP;
        GameType gameType = GameType.N_PUZZLE;
        String file = "commands.txt";
        int gameSize = 3;

        if (args.length == 0) {
            // no command-line arguments
            // enter REPL mode
            mode = Mode.LOOP;
            gameType = GameType.N_PUZZLE;
        }
        else if (args.length == 1) {
            // enter file mode
            mode = Mode.FILE;
            gameType = GameType.N_PUZZLE;
        }
        else if (args.length >= 2) {
            // parse explicit arguments
            try {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("--file")) {
                        mode = Mode.FILE;
                        file = args[i+1];
                    }
                    if (args[i].equals("--game-type")) {
                        gameType = GameType.stringToGameType(args[i+1]);
                    }
                    if (args[i].equals("--game-size")) {
                        gameSize = Integer.parseInt(args[i+1]);
                    }
                }
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        else {
            throw new IllegalArgumentException(String
                    .format("Too many (%d) arguments passed in. Only zero or one is expected.", args.length));
        }

        // now we have mode, file, gameType
        if (mode.equals(Mode.FILE)) {
            fileMode(file, gameType, gameSize);
        }
        if(mode.equals(Mode.LOOP)) {
            loopMode(gameType, gameSize);
        }

    }
}
