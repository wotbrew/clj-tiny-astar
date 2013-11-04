(ns clj-tiny-astar.path
  (:use [clj-tuple]
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

(defn manhattan-dist
  [[x0 y0] [x1 y1]]
  (+ (Math/abs ^int(- x1 x0)) (Math/abs ^int(- y1 y0))))


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

(defn initial-open-set
  [start]
  (priority-map start 0))

(def first-first (comp first first))
(defrecord Square [loc g h parent])
(defn f [square] (+ (:g square) (:h square)))

(defn a*-adj-squares
  [pred closed loc goal]
  (->> (adj loc)
       (filter #(and (pred %)
                     (not (closed %))))
       (map (fn [p]
              (->Square p
                        (if (diagonal? loc p) 14 10)
                        (manhattan-dist p goal)
                        loc)))))

(defn a*-collect-path
  [square sq-map]
  (loop [acc [(:loc square)]
         sq square]
    (if-let [parent (sq-map (:parent sq))]
      (recur (conj acc (:loc parent)) parent)
      acc)))

(defn a*
"find a path from a to b given the predicate function 'pred'.
pred is a function of point -> bool"
  [pred a b]
  (loop [open (initial-open-set b)
         squares {b (->Square b 0 0 nil)}
         closed #{}]
    (if-not (empty? open)
      (let [curr (first-first open)
            curr-square (squares curr)]      
        (if (= curr a)
          (a*-collect-path curr-square squares)
          (let [adj (a*-adj-squares pred closed curr a)             
                reducer (fn [[squares open :as data] a]
                          (let [loc (:loc a)]
                            (cond
                             (not (open loc))
                             (tuple (assoc squares loc a)
                                    (assoc open loc (f a)))
                             (< (:g a) (:g (squares loc)))
                             (tuple (assoc squares loc a)
                                    open)
                             :else data)))
                [new-squares new-open] (reduce reducer (tuple squares open) adj)]
            (recur (dissoc new-open curr) new-squares (conj closed curr))))))))





