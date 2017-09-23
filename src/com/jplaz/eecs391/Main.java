package com.jplaz.eecs391;

import java.util.Scanner;

public class Main {

    private static void loopMode() {
        Scanner inputScanner = new Scanner(System.in);
        while(true) {
            String command = inputScanner.nextLine();
            System.out.println("You entered: " + command);
        }
    }

    private static void fileMode(String filename) {

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            // no command-line arguments
            // enter REPL mode
            loopMode();
        }
        else if (args.length == 1) {
            // enter file mode
            fileMode(args[0]);
        }
        else {
            throw new IllegalArgumentException(String
                    .format("Too many (%d) arguments passed in. Only zero or one is expected.", args.length));
        }
    }
}
