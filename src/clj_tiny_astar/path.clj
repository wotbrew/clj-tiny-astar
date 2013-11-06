(ns clj-tiny-astar.path
  (:use [clj-tuple]
        [clj-tiny-astar.util]
        [clojure.data.priority-map :only [priority-map]])
  (:import [java.lang Math]))



(defn initial-open-set
  [start]
  (priority-map start 0))

(def first-first (comp first first))
(defrecord Square [loc g h parent])
(defn f [square] (+ (:g square) (:h square)))

(defn a*-adj-squares
  [dist pred closed loc goal]
  (->> (adj loc)
       (filter #(and (pred %)
                     (not (closed %))))
       (map (fn [p]
              (->Square p
                        (if (diagonal? loc p) 1.4 1)
                        (dist p goal)
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
([dist pred a b]
  (loop [open (initial-open-set b)
          squares {b (->Square b 0 0 nil)}
          closed #{}]
     (if-not (empty? open)
       (let [curr (first-first open)
             curr-square (squares curr)]      
         (if (= curr a)
           (a*-collect-path curr-square squares)
           (let [adj (a*-adj-squares dist pred closed curr a)             
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
([pred a b]
   (a* manhattan-dist pred a b)))





