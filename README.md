clj-tiny-astar
==============

[![Build Status](https://travis-ci.org/danstone/clj-tiny-astar.png?branch=master)](https://travis-ci.org/danstone/clj-tiny-astar)


Little a* pathfinder for 2D binary grids. Diagonals are punished, and distance estimation is performed by default using the manhattan heuristic.

**lein:**

```clojure

[clj-tiny-astar "0.1.1-SNAPSHOT"]

```

**Usage:**

Clone into a local repository and:

```clojure

;; this is designed to operate on bounded grids

(clj-tiny-astar/a*
   [4 4] ;;bounds of the grid (width & height)
   my-predicate ;;predicate that takes a point [x y] and returns whether a cell is walkable
   [0 0] ;;from
   [3 3] ;;to)

;;example result, diagonals are punished:

[[0 0] [1 1] [2 2] [3 3]]

```

the predicate function takes a point (tuple of x and y).


