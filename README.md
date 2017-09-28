# EECS 391: Programming Project 1

## Design Choices

### Object-Oriented Design

My project is divided into two primary classes: NPuzzleState and GameSolver. The NPuzzleState class implements the game state and the logic to make moves. The GameSolver class implements the search algorithms that operates on instances of NPuzzleState. I had intended to design NPuzzleState to generalize beyond the 8-puzzle to the 15-puzzle and more, but currently only the 8-puzzle is supported. The GameSolver class is designed to operate on a general GameState subclass, but currently only supports NPuzzleState. It would be simple to generalize, but since I have not yet implemented other GameState classes, there is no need. 

### Classes

#### Main.java

This is the entry point of the program. It cointains the `main` function which is called when the executable is invoked. The main function is responsible for parsing the command line arguments to the program. This determines the mode, gameType, gameSize, and file of the program. 

The mode may be either loop mode or file mode. Loop mode is like a REPL- it listens for commands issued to stdin, modifies the program state, and then prompts for another command. File mode looks for a given filename and attempts to open the file. The file should contain the same commands that one would enter into loop mode, where each command is on a new line. It is not possible to switch modes once the program has loaded. 

The gameType parameter determines which game to play. For now, only NPuzzle is supported. 

The gameSize parameter determines the size of the game, in terms of one dimension. For the 8-puzzle, this is 3 because it is played on a 3x3 grid. For now, only 3 is supported. 

The file of the program is the name of the file that file mode attempts to open and read through. For best results, put the text file in the same directory as the Main.class file and only pass in the file name, e.g. `"commands.txt"`. Relative paths have not been tested.

The patterns for invoking the program are described in the following table:

|Command|Effect|
|-------|------|
|java com.jplaz.eecs391.Main|mode = loop, gameType = NPuzzle, gameSize = 3 (default)|
|java com.jplaz.eecs391.Main commands.txt|mode = file, gameType = NPuzzle, gameSize = 3|
|java com.jplaz.eecs391.Main --game-type NPuzzle --game-size 3 |mode = loop, gameType = NPuzzle, gameSize = 3|
|java com.jplaz.eecs391.Main --game-type NPuzzle --game-size 3 --file commands.txt|mode = file, gameType = NPuzzle, gameSize = 3|

#### Command.java

This is an Enum for matching command strings to functions. The mapping is stored in the Enum, and exposes a function that accepts a string of a command and its arguments, and calls its associated function with its arguments. Since there are a fixed number of commands, it makes sense to enumerate them in an enum. Additionally, this enum provides a `stringToCommand` function that parses the command string into the enum type. 

#### Move.java

For the same reasons as above, Move.java enumerates the possible moves of the NPuzzle. Ideally, Move would be an interface and each puzzle would implement Move accordingly, but for now, these moves are for the NPuzzleState (up, down, left, right). 

#### NPuzzleState.java

A big question that was addresssed during design is: Should GameState objects be mutable or immutable? It seems like managing unique states in terms of marked/unmarked states would be much easier if each instance was a unique and immutable state. The GameSolver class should be able to easily create and manipulate the state of the game by manipulating instances rather than a single object. This is implemented by making deep copies of the `gameBoard` array when making moves. Additionally, `equals()` and `hashCode()` are overridden so that when an instance of NPuzzleState is added to a hash map, another instance with the same game state will be considered equal. 

#### GameSolver.java

This class implements the search algorithms. It operates on GameState objects.

### Representing the 8-puzzle Game State

A big question that was addressed when implementing NPuzzleState is: how should game states be represented? The input and output representations are strings, but this is not the best choice for the actual representation that the movement methods will operate on. In order to take advantage of the mathematical rules governing game play, it makes sense to use numbers instead of characters. Specifically, a `short[]` is used, since the values are bounded to 0-8 for the 8-puzzle and are generally sufficiently small. This choice saves some memory compared to `int[]`, and gives constant time access. The board size is fixed, so no insertions or deletions are performed.  

This provides an easy way to arithmetically determine which moves are valid: 

```
private boolean isValidMove(Move move) {
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
```

First, a helper method is used to find the index of the blank tile. Then, some arithmetic is done based on that index which determines whether or not the blank tile can be moved in a given direction. For example, if the blank space is "on the bottom row", it cannot be moved down. For the 8-puzzle, "on the bottom row" means that the index is greater than 5. The indices 0-5 are the top two rows, and the indices 6-8 are the bottom row. Similar definitions are used for the other moves. 

To get a list of valid moves, simply take each move, and if it is valid, add it to a list. This makes finding the successor states as simple as calling one method, `getValidMoves()`.

### Managing Game State

There are a few public methods of NPuzzleState governing tile movement that are used by the GameSolver class. These are `randomizeState(), move(), and getValidMoves()`. Additionally, the public methods `printState()` and `isGoalState()` are defined, as well as getters and setters for the `gameBoard` (state) and `pathCost` member fields. 

### A* Search

#### Tracking Path Cost

#### Heuristic Selection/Implementation

The heuristic function is used to order nodes in the priority queue. Java's PriorityQueue allows the programmer to supply an instance of the Comparator interface to determine an ordering of the objects in the queue. Therefore, it is natural to define and choose the heuristic within the comparator. To do this, Comparator is implemented by GameComparator, which defines a constructor that accepts as input a string and stores it as a local fied. When making a comparison, the GameComparator checks the value of that string ("h1" or "h2") and applies the heuristic (an instance method of the NPuzzleState class) accordingly. 

### Beam Search

#### Cost Function Selection/Implementation

For the cost function, the `h2` heuristic from A* search is reused. This function worked well for A* and is a good choice of how good a given board state is. Plus, no extra code is required because the same NPuzzleState objects are used. 

## TODO, Eventually

* clean up array copying and use final
* set state to goal after solving/fix bug on solving twice
* Clean up switch statements
* Abstract class/Interface for GameSolver
* Interface for Move
* rethink gameStateCache data structure choice
* fix input parsing to not be as hacky (split on non-quoted characters)
* make manhattan distance not hard-coded
* fix interface(s)
