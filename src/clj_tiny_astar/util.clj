(ns clj-tiny-astar.util
  (use [clj-tuple]
       [clojure.data.priority-map :only [priority-map]])
  (:import [java.lang Math]))


(defrecord Directions
    [n ne e se s sw w nw])

(defn mapsnd
  [f [a b]]
  (tuple a (f b)))

(defn mapvals
  [f m]
  (into {} (map (partial mapsnd f) m)))

(defn pairs
  [seq]
  (partition 2 seq))

(def direction-offsets
  (let [raw [0 -1
             1 -1
             1 -1
             1 1
             0 1
             -1 1
             -1 0
             -1 -1]
        split (pairs raw)]
    (map (fn [[x y]] (tuple x y)) split)))

(def directions
  (let [dirs [:n :ne :e :se :s :sw :w :nw]]
    (map->Directions (into {} (zipmap dirs direction-offsets)))))

(def direction-offsets
  (map second directions))

(defn vec-add
  [[x y] [x2 y2]]
  (tuple (+ x x2) (+ y y2)))

(defmacro xor
  ([] false)
  ([a] true)
  ([a b]
     `(if ~a
        (if ~b false true)
        (if ~b true false)))
  ([a b & more]
     `(xor  (xor ~a ~b) (xor ~@more))))

(defn diagonal?
  [[x1 y1] [x2 y2]]
  (let [a (= (- x1 x2) 0)
        b (= (- y1 y2) 0)]
   (not (xor a b))))

(defn adj
  [p]
  (map #(vec-add p %) direction-offsets))

