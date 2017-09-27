# EECS 391: Programming Project 1

## Design Choices

### Object-Oriented/Generic Design

My intent is to complete the extra credit, and to generally write reusable code. Therefore, there should be an interface that is implemented on a per-game basis. It should look something like this:

```
Fields:
a field to store the board state

Functions:
move(state, from, to) [in the 8-puzzle, from is always the blank space]
isValidMove(state, from, to)
isGoalState(state)
randomizeState(state)
solveAStar(state, heuristic, maxNodes)
solveBeam(state, localStates, maxNodes)
evaluateCostFunction(state, function)
```

### Classes

#### Main.java

This is the entry point of the program. It cointains the `main` function which is called when the executable is invoked. The `main` function expects one argument, the 

#### Commands.java

This is an Enum for matching command strings to functions. The mapping is stored in the Enum, and exposes a function that accepts a string of a command and its arguments, and calls its associated function with its arguments. 

|Command|How it is applied|
|---|---|---|



#### NPuzzleState.java

There is no constructor; the game state is not initialized until `setState` or `randomizeState` is called. A design choice to make is: Should GameState objects be mutable or immutable? It seems like managing unique states in terms of marked/unmarked states would be much easier if each instance was a unique and immutable state. The GameSolver class should be able to easily create and manipulate the state of the game by manipulating instances rather than a single object. 

However, we do not really need N object instances for N states. Because the state of a GameState object can be uniquely represented by its `gameBoard` field, we can reuse a single instance to represent the current state of the game. When searching, that is, dealing with successor states, we are still free to create new objects. 

#### GameSolver.java

This class implements the search algorithms. It operates on GameState objects. 


### Representing the 8-puzzle Game State

How to represent game state? The input and output representations are strings, but this is not the best choice for the actual representation. In order to take advantage of the mathematical rules governing game play, it makes sense to use numbers instead of characters. Specifically, a `short[]` is used, since the values are bounded to 0-8, and can be easily and quickly indexed. 

### Game State Evolution


## Execution Flow

### Notes

1. Initialize Game State
1. Read commands from a file 
2. Parse commands into functions
3. Represent game state 


#### Running the Code 

##### Interactive (REPL) Mode

```
> java projectOne
> "Welcome to GameSearchSolver, please enter a valid command: "
> 
```

##### File Mode

```
> java projectOne commands.txt
```

## TODO, Eventually

* Clean up switch statements
* Abstract GameState class 
* Convert Command HashMap to Enum
* fix input parsing to not be as hacky (split on non-quoted characters)
* make manhattan distance not hard-coded
* fix interface(s)
