## Rubiks Cube

### Modifications to Gameplay Interface

A new command `play` has been added: 

`play rubiks` initializes a Rubik's Cube.

`play 8-puzzle` initializes an 8-puzzle.


## TODO

* refactor into GameSolver/abstract GameState class:
	* randomizeState and makeRandomMove
	* printPath
	* overridden methods (equals, hashCode)
* implement another heuristic
* do something about undo moves (CW -> CCW on the same side)
* come up with a better string representation of the 2x2x2 cube
* the file parsing interface needs to know about RubiksPuzzle
* stringToMove methods should throw exceptions instead of returning null