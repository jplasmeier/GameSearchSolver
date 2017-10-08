## Rubiks Cube

### Move Tables

The front face is `[12, 13, 14, 15]`.

#### FACE CW

```
02 -> 10
03 -> 11

10 -> 18
11 -> 19

12 -> 13
13 -> 14
14 -> 15
15 -> 12

18 -> 22
19 -> 23

22 -> 02
23 -> 03
```

#### FACE CCW

```
02 -> 22
03 -> 23

10 -> 02
11 -> 03

12 -> 15
13 -> 12
14 -> 13
15 -> 14

18 -> 10
19 -> 11

22 -> 18
23 -> 19
```

## TODO

* the CLI/file interface + downstream code (e.g.applyCommand) needs to know about side