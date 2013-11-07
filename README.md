clj-tiny-astar
==============

Little a* pathfinder for 2D binary grids. Diagonals are punished, and distance estimation is performed by default using the manhattan heuristic.

**lein:**

```clojure

[clj-tiny-astar "0.1.0-SNAPSHOT"]

```

**Usage:**

Clone into a local repository and:

```clojure

(clj-tiny-astar/a* my-predicate [0 0] [3 3])

;;example result, diagonals are punished:

[[0 0] [1 1] [2 2] [3 3]]

```

the predicate function takes a point (tuple of x and y).


