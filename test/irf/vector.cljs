(ns test.irf.vector
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [irf.vector :as v]))

(enable-console-print!)

;; helper
(def abs js/Math.abs)

(defn rad-to-deg [rad]
  (let [pi (.-PI js/Math)]
    (js/parseFloat
      (.toFixed
        (* rad (/ 360 (* 2 pi)))
        2))))

(testing "almost equal"
  (deftest almost-equal
    (is (= (v/almost= 100 101) false))
    (is (v/almost= 1))
    (is (v/almost= 1 1))
    (is (v/almost= 1 1 1.0000001))
    (is (v/almost= 10000001 10000002))))

(testing "addition"
  (deftest add
    (is (= (v/add [0 0])
           [0 0]))
    (is (= (v/add [2 3] [4 5])
           [6 8]))
    (is (= (v/add [1 2] [3 4] [5 6])
           [9 12]))))

(testing "subtraction"
  (deftest vector-subtract
    (is (= (v/subtract [1 1]) [1 1]))
    (is (= (v/subtract [10 20] [2 5])
           [8 15]))
    (is (= (v/subtract [10 20] [2 5] [1 1])
           [7 14]))))

(testing "multiplication with scalar"
  (deftest mult
    (is (= (v/mult [4 7] 3)
           [12 21]))))

(testing "division with scalar"
  (deftest div
    (is (= (v/div [14 21] 7)
           [2 3]))))

(testing "getting the (squared) length of a vector"
  (deftest length-squared
    (is (= (v/length-squared [3 4]) (+ 9 16))))
  (deftest length
    (is (= (v/length [3 4]) 5))))

(testing "normalizing a vector = trimming it to length of 1"
  (deftest norm
    (is (= (v/length
             (v/norm [17 19]))
           1))))

(testing "dot product aka scalar product"
  (deftest dot
    (is (= (v/dot [3 5] [7 9])
           (+ 21 45)))))

(testing "collinearity"
  (deftest collinear
    (is (= (v/collinear? [4 -6] [6 -9] [2 -3])
           true))
    (is (= (v/collinear? [10 20] [0 0])
           true))
    (is (= (v/collinear? [4 5] [7 6])
           false))))

(testing "angle between two vectors"
  (deftest angle
    (is (= (rad-to-deg (v/angle [1 5] [3 7]))
           11.89))
    (is (= (rad-to-deg (v/angle [3 7] [1 5]))
           11.89))))

(testing "projection"
  (deftest project
    (is (v/v= (v/project [-1 5] [4 6])
           [2 3]))
    (is (= (v/collinear?
             (v/project [1 2] [-1 -5])
             [-1 -5])
           true))))

