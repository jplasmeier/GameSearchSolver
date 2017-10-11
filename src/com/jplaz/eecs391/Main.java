package com.jplaz.eecs391;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static GameSolver solver = new GameSolver();

    private static void parseLineAndApply(String line) throws Exception {
        String input_tokens[] = line.split(" ", 2);
        String input_command = input_tokens[0];
        String input_arguments = "";
        if (input_tokens.length > 1) {
            input_arguments = input_tokens[1];
        }
        Command command = Command.stringToCommand(input_command);
        solver.applyCommand(command, input_arguments);
    }

    private static void loopMode(GameType gameType, int gameSize) throws Exception {
        System.out.println("Welcome to GameSearchSolver. Please enter a command.");
        Scanner inputScanner = new Scanner(System.in);
        while(true) {
            String input_line = inputScanner.nextLine();
            parseLineAndApply(input_line);
        }
    }

    private static void fileMode(String filename, GameType gameType, int gameSize) throws Exception {
        System.out.println("File mode selected. Opening " + filename);
        try {
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                parseLineAndApply(line);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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
            file = args[0];
        }
        // this might not work at all lol
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
            throw new IllegalArgumentException("Invalid argument(s).");
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
