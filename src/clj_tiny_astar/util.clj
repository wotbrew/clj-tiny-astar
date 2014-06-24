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
             1 0
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
  (let [a (zero? (- x1 x2))
        b (zero? (- y1 y2))]
   (not (xor a b))))

(defn in-bounds?
  [w h [x y]]
  (and (< -1 x w)
       (< -1 y h)))

(defn adj
  [w h p]
  (->> (map #(vec-add p %) direction-offsets)
       (filter #(in-bounds? w h %))))
