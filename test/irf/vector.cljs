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

(testing "vector creation"
  (deftest new-vector
    (is (= (v/v 123 456) {:x 123 :y 456}))
    (is (= (apply v/v [23 42]) {:x 23 :y 42}))))

(testing "quadrant"
  (deftest quadrant
    (is (= 1 (v/quadrant (v/v 12 34))))
    (is (= 2 (v/quadrant (v/v 6 -3))))
    (is (= 3 (v/quadrant (v/v -5 -9))))
    (is (= 4 (v/quadrant (v/v -1 2))))))

(testing "almost equal"
  (deftest almost-equal
    (is (= (v/almost= 100 101) false))
    (is (v/almost= 1))
    (is (v/almost= 1 1))
    (is (v/almost= 1 1 1.00000000001))
    (is (v/almost= 100000000001 100000000002))))

(testing "addition"
  (deftest add
    (is (= (v/add v/v0)
           v/v0))
    (is (= (v/add (v/v 2 3) (v/v 4 5))
           (v/v 6 8)))
    (is (= (v/add (v/v 1 2) (v/v 3 4) (v/v 5 6))
           (v/v 9 12)))))

(testing "subtraction"
  (deftest vector-subtract
    (is (= (v/subtract (v/v 1 1)) (v/v 1 1)))
    (is (= (v/subtract (v/v 10 20) (v/v 2 5))
           (v/v 8 15)))
    (is (= (v/subtract (v/v 10 20) (v/v 2 5) (v/v 1 1))
           (v/v 7 14)))))

(testing "multiplication with scalar"
  (deftest mult
    (is (= (v/mult (v/v 4 7) 3)
           (v/v 12 21)))))

(testing "division with scalar"
  (deftest div
    (is (= (v/div (v/v 14 21) 7)
           (v/v 2 3)))))

(testing "getting the (squared) length of a vector"
  (deftest length-squared
    (is (= (v/length-squared (v/v 3 4)) (+ 9 16))))
  (deftest length
    (is (= (v/length (v/v 3 4)) 5))))

(testing "normalizing a vector = trimming it to length of 1"
  (deftest norm
    (is (= (v/length
             (v/norm (v/v 17 19)))
           1)
        (= (v/length
             (v/norm (v/v -3 7)))
           1))))

(testing "dot product aka scalar product"
  (deftest dot
    (is (= (v/dot (v/v 3 5) (v/v 7 9))
           (+ 21 45)))))

(testing "collinearity"
  (deftest collinear
    (is (= (v/collinear? (v/v 4 -6) (v/v 6 -9) (v/v 2 -3))
           true))
    (is (= (v/collinear? (v/v 10 20) (v/v 0 0))
           true))
    (is (= (v/collinear? (v/v 4 5) (v/v 7 6))
           false))))

(testing "angle between two vectors"
  (deftest angle
    (is (= (rad-to-deg (v/angle (v/v 0 1) (v/v 1 0)))
           90))
    (is (= (rad-to-deg (v/angle (v/v 1 1) (v/v 1 0)))
           45))
    (is (= (rad-to-deg (v/angle (v/v 1 0) (v/v 1 1)))
           (- 360 45) 315))
    (is (= (rad-to-deg (v/angle (v/v 1 1) (v/v 1 -1)))
           90))
    (is (= (rad-to-deg (v/angle (v/v 1 1) (v/v -1 -1)))
           180))
    (is (= (rad-to-deg (v/angle (v/v 1 -1) (v/v 1 1)))
           270))
    (is (= (rad-to-deg (v/angle (v/v -1 1) (v/v 1 1)))
           90))
    (is (= (rad-to-deg (v/angle (v/v 1 1) (v/v -1 1)))
           (- 360 90)))
    (is (= (rad-to-deg (v/angle (v/v 1 5) (v/v 3 7)))
           11.89))
    (is (= (rad-to-deg (v/angle (v/v 3 7) (v/v 1 5)))
           (- 360 11.89)))))

(testing "projection"
  (deftest project
    (is (v/v= (v/project (v/v -1 5) (v/v 4 6))
           (v/v 2 3)))
    (is (= (v/collinear?
             (v/project (v/v 1 2) (v/v -1 -5))
             (v/v -1 -5))
           true))))

(testing "intersection: intersect function"
  (deftest intersection
    (is (= (v/intersection (v/v 3 6) (v/v 2 -10) (v/v 2 -2) (v/v 4 6)) ; intersecting
               (v/v 4 1)))
    (is (= (v/intersection (v/v 2 2) (v/v 4 2) (v/v 2 -5) (v/v 1 4)) ; intersecting outside the vector
               (v/v 4 3)))
    (is (= (v/intersection (v/v 2 2) (v/v 5 1) (v/v 1 -1) (v/v -5 -1)) ; collinear
               (v/v Infinity Infinity)))))

(testing "intersection: intersects test"
  (deftest intersects
    (is (= (v/intersect? (v/v 3 6) (v/v 2 -10) (v/v 2 -2) (v/v 4 6)) ; intersecting
           (v/v 4 1)))
    (is (= (v/intersect? (v/v 2 2) (v/v 4 2) (v/v 2 -5) (v/v 1 4)) ; intersecting outside the vector
           nil))
    (is (= (v/intersect? (v/v 2 2) (v/v 5 1) (v/v 1 -1) (v/v -5 -1)) ; collinear
           nil))))

