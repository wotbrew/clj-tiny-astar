clj-tiny-astar
==============

Little a* pathfinder for 2D binary grids. Diagonals are punished, and distance estimation is performed using the manhattan heuristic.

**Usage:**

Clone into a local repository and:

```clojure

(clj-tiny-astar/a* my-predicate [0 0] [3 3])

;;example result, diagonals are punished:

[[0 0] [0 1] [0 2] [0 3] [1 3] [2 3] [3 3]]

```




