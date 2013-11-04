(ns clj-tiny-astar.path-test
  (:use [clojure.test]
        [clj-tiny-astar.path]))

(def test-grid
  ;0 1 2 3 4 5
  [0 0 0 0 0 0 ; 0
   0 0 0 1 1 0 ; 1
   0 0 1 1 1 0 ; 2
   0 0 0 0 1 0 ; 3
   0 0 0 0 1 0 ; 4
   ])

(def test-width 6)

(defn can-walk?
  [[x y]]
  (let [i (+ x (* y test-width))]
    (and (< -1 i)
         (< i (count test-grid))
         (= (test-grid i) 0))))

(deftest walk1
  (is (=  [[2 1] [3 0] [4 0] [5 1]]
          (a* can-walk? [2 1] [5 1]))))
