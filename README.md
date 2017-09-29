# EECS 391: Programming Project 1

by Justin Plasmeier (jgp45)

## Code Design 📐

### Object-Oriented Design

My project is divided into two primary classes: NPuzzleState and GameSolver. The NPuzzleState class implements the game state and the logic to make moves. The GameSolver class implements the search algorithms that operates on instances of NPuzzleState. I had intended to design NPuzzleState to generalize beyond the 8-puzzle to the 15-puzzle and more, but currently only the 8-puzzle is supported. The GameSolver class is designed to operate on a general GameState subclass, but currently only supports NPuzzleState. It would be simple to generalize, but since I have not yet implemented other GameState classes, there is no need. 

### Classes

#### Main.java

This is the entry point of the program. It contains the `main` function which is called when the executable is invoked. The main function is responsible for parsing the command line arguments to the program. This determines the mode, gameType, gameSize, and file of the program. 

The mode may be either loop mode or file mode. Loop mode is like a REPL- it listens for commands issued to stdin, modifies the program state, and then prompts for another command. File mode looks for a given filename and attempts to open the file. The file should contain the same commands that one would enter into loop mode, where each command is on a new line. It is not possible to switch modes once the program has loaded. 

The gameType parameter determines which game to play. For now, only NPuzzle is supported. 

The gameSize parameter determines the size of the game, in terms of one dimension. For the 8-puzzle, this is 3 because it is played on a 3x3 grid. For now, only 3 is supported. 

The file of the program is the name of the file that file mode attempts to open and read through. For best results, put the text file in the `out/production/Project1/` directory (top-level relative to the namespace, i.e. same directory as the `com` folder) and only pass in the file name, e.g. `"commands.txt"`. Relative paths have not been tested. 

The patterns for invoking the program are described in the following table:

|Command|Effect|
|-------|------|
|java com.jplaz.eecs391.Main|mode = loop, gameType = NPuzzle, gameSize = 3 (default)|
|java com.jplaz.eecs391.Main commands.txt|mode = file, gameType = NPuzzle, gameSize = 3|

#### Command.java

This is an Enum for matching command strings to functions. The mapping is stored in the Enum, and exposes a function that accepts a string of a command and its arguments, and calls its associated function with its arguments. Since there are a fixed number of commands, it makes sense to enumerate them in an enum. Additionally, this enum provides a `stringToCommand` function that parses the command string into the enum type. 

#### Move.java

For the same reasons as above, Move.java enumerates the possible moves of the NPuzzle. Ideally, Move would be an interface and each puzzle would implement Move accordingly, but for now, these moves are for the NPuzzleState (up, down, left, right). 

#### NPuzzleState.java

A big question that was addressed during design is: Should GameState objects be mutable or immutable? It seems like managing unique states in terms of marked/unmarked states would be much easier if each instance was a unique and immutable state. The GameSolver class should be able to easily create and manipulate the state of the game by manipulating instances rather than a single object. This is implemented by making deep copies of the `gameBoard` array when making moves. Additionally, `equals()` and `hashCode()` are overridden so that when an instance of NPuzzleState is added to a hash map, another instance with the same game state will be considered equal. 

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

#### Walkthrough

The aStarSearch method (camel-cased by Java convention) takes as input a string representing the heuristic to use and an NPuzzleState instance representing the initial state. 

A HashMap storing `short[]` instances as keys and NPuzzleStates as values is initialized to an empty HashMap. This will be used to store gameStates that have been discovered but not yet processed. This prevents the algorithm from instantiating duplicate states.

An ArrayList storing NPuzzleStates, `staleStates`, is initialized to an empty ArrayList. This will store the states that have been discovered and processed. This prevents loops. 

Next, the Comparator and Priority Queue are declared and instantiated (see Heuristic Selection/Implementation for more). 

An iteration counting integer is set to 0, the initial state is added to the queue and stateCache, and its path cost is set to 0. Finally, all of the necessary data structures have been initialized.

Now, the main while loop is entered. Each iteration takes a node off of the queue and processes it. First, it is checked for being a goal state. Next, the number of iterations is checked against `maxNodes`, and failure is returned if the limit is reached. Next, the current node is added to the `staleStates`. We don't want to process it twice, so this makes sure that the current node will not be added to the queue. The number of iterations is incremented. 

Next, the `getValidMoves()` function is called, returning an ArrayList of Move instances (rather, Move enum options? I am not sure that these are still considered instances...). A for-each loop iterates through the valid moves. 

For each move, an NPuzzleState is either fetched from the cache or instantiated, if it is a brand new state. If it is a brand new state, its path is set to the current node's path cost (the one most recently polled from the queue) plus the move that was made to produce the new state. 

If this new state exists in the `staleStates` list, further processing is skipped. It already has been processed. 

Next, the path cost is updated. This is either the first time it has been set (for brand new states), or the cached state's path cost is compared to the current node's path cost (plus one for the move to produce the new state) and if it is less, the path is replaced. This way, if a shorter path to an existing state is found, it will be updated to one with a lesser cost. 

Finally, if the new state is not already in the queue, add it. The Comparator takes care of the cost function based on the node's path cost and the heuristic chosen on initialization. The while loop repeats until one of the return conditions (maxNodes reached, goalState found, no moves left) or the queue is empty. If the queue empties, it is considered failure. 

#### Tracking Path/Cost

In order to track the path, the NPuzzleState object has a LinkedList field to store successive moves. LinkedList was chosen for quick appends, since each time a move is made, an append operation will occur. 

#### Heuristic Selection/Implementation

The heuristic function is used to order nodes in the priority queue. Java's PriorityQueue allows the programmer to supply an instance of the Comparator interface to determine an ordering of the objects in the queue. Therefore, it is natural to define and choose the heuristic within the comparator. To do this, Comparator is implemented by GameComparator, which defines a constructor that accepts as input a string and stores it as a local field. When making a comparison, the GameComparator checks the value of that string ("h1" or "h2") and applies the heuristic (an instance method of the NPuzzleState class) accordingly.  

### Beam Search

#### Cost Function Selection/Implementation

For the cost function, the `h2` heuristic from A* search is reused. This function worked well for A* and is a good choice of how good a given board state is. Plus, no extra code is required because the same NPuzzleState objects are used. 

## Experiments 🔎

### a - relation between maxNodes and randomizeState

Generally, the larger the number of random moves, the larger the maxNodes must be. However, after sufficiently many random moves, it is, well, random as to how many nodes will be required. 

To run these experiments, use file mode with the files provided (e.g. experiment-a1.txt).

The general conclusion is that more random moves only very generally results in a "harder" puzzle at small numbers of moves. Once sufficiently many random moves are made, there is a bound on the number of nodes required to solve the puzzle. A script could be written to draw a graph and visually represent this bound, but I admit am running low on time, so generated text files must suffice. 

For A-star h1, about 10,000 nodes is enough for most puzzles. However, to solve all puzzles (that were tested), as many as 40,000 nodes are required, which takes much longer. 

For A-star h2, 1000 nodes will solve most puzzles, and 10,000 seems to solve all.

For beam search with k = 50, it appears to be no more than 50 nodes. For beam search with k = 5, the constraint seems to be on k; that is, no amount of maxNodes helps `beam 5` find a solution in some cases. Setting k = 15 appears to be sufficient, and this too requires about 50 nodes max to ensure success in all trials. 

A note on memory: Java seems to allocate memory amongst multiple processes which made tracking memory usage difficult. Beam search ran too quickly to even observe the process being created. A-star h1 used less than 200mb at its worst; and A-star h2 about 50mb. At no point did the memory required to solve a puzzle exceed the amount of memory that my IDE was using (a whopping 1.2Gb!!)...

### b - which A* heuristic is better?

The `h2` heuristic, manhattan distance is significantly better. In the above experiment, h2 is able to find the solution using far fewer nodes. 

### c - relative solution length

Assuming that "solution length" refers to the amount of time, or rather the number of nodes required to find a solution, A* h1 is much slower than A* h2, and both are significantly slower than beam search- provided k is high enough (about 15). 

### d - solvability?

This depends on the maxNodes and on k for beam search. When high enough, I did not encounter any unsolvable puzzles. It is possible that there are states that would best my Macbook, but I did not find any. 

## Discussion 💁🏼

In my experience, beam search was much faster than either, by one or more orders of magnitude. A-star with h2 was reasonably quick, and A-star h1 was a bit slow. 

Beam search was also easier to implement. This may be due to the fact that I had already had the experience of implementing A-star; A-star was a bit tricky. Overall, implementing the game state and all of the necessary auxiliary functionality was both more time-consuming and honestly perhaps more difficult. Implementing an object-oriented design scheme is typically more difficult than implementing a single function. Though, I suppose I could have spent less time on the auxiliary parts, it would have resulted in more difficulty when implementing the actual search algorithms. 

## TODO 📋

_Hofstadter's Law: It always takes longer than you expect, even when you take into account Hofstadter's Law._

- Douglas Hofstadter, _Gödel, Escher, Bach: An Eternal Golden Braid_

* clean up array copying and use final
* Clean up switch statements
* Abstract class/Interface for GameSolver
* Interface for Move
* rethink gameStateCache data structure choice
* fix input parsing to not be as hacky (split on non-quoted characters)
* make manhattan distance not hard-coded
* fix GameState interface
