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

### Representing the 8-puzzle Game State

How to represent game state? The input and output representations are strings. It may be more convenient or efficient to represent the state differently within the program. 

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