(ns test.irf.vector
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [irf.vector :as v]
            [irf.util :as util]))

(enable-console-print!)

(testing "quadrant"
  (deftest quadrant
    (is (= 1 (v/quadrant (v/->Vector 12 34))))
    (is (= 2 (v/quadrant (v/->Vector 6 -3))))
    (is (= 3 (v/quadrant (v/->Vector -5 -9))))
    (is (= 4 (v/quadrant (v/->Vector -1 2))))))

(testing "almost equal"
  (deftest almost-equal
    (is (not (v/almost= 100 101)))
    (is (v/almost= 1))
    (is (v/almost= 1 1))
    (is (v/almost= 1 1 1.00000000001))
    (is (v/almost= 100000000001 100000000002))
    (is (v/almost= (v/->Vector 1 1) (v/->Vector 1 1)))
    (is (not (v/almost= (v/->Vector 1.0001 1) (v/->Vector 1 1))))
    (is (v/almost= (v/->Vector 1 1.00000000001)
                   (v/->Vector 1 1.00000000002)
                   (v/->Vector 1 1.00000000003)))))

(testing "addition"
  (deftest add
    (is (= (v/add v/v0) 
           v/v0))
    (is (= (v/add (v/->Vector 2 3) (v/->Vector 4 5))
           (v/->Vector 6 8)))
    (is (= (v/add (v/->Vector 1 2) (v/->Vector 3 4) (v/->Vector 5 6))
           (v/->Vector 9 12)))))

(testing "subtraction"
  (deftest vector-subtract
    (is (= (v/subtract (v/->Vector 1 1)) (v/->Vector 1 1)))
    (is (= (v/subtract (v/->Vector 10 20) (v/->Vector 2 5))
           (v/->Vector 8 15)))
    (is (= (v/subtract (v/->Vector 10 20) (v/->Vector 2 5) (v/->Vector 1 1))
           (v/->Vector 7 14)))))

(testing "multiplication with scalar"
  (deftest mult
    (is (= (v/mult (v/->Vector 4 7) 3)
           (v/->Vector 12 21)))))

(testing "division with scalar"
  (deftest div
    (is (= (v/div (v/->Vector 14 21) 7)
           (v/->Vector 2 3)))))

(testing "getting the (squared) length of a vector"
  (deftest length-squared
    (is (= (v/length-squared (v/->Vector 3 4)) (+ 9 16))))
  (deftest length
    (is (= (v/length (v/->Vector 3 4)) 5))))

(testing "normalizing a vector = trimming it to length of 1"
  (deftest norm
    (is (= (v/length
             (v/norm (v/->Vector 17 19)))
           1)
        (= (v/length
             (v/norm (v/->Vector -3 7)))
           1))))

(testing "dot product aka scalar product"
  (deftest dot
    (is (= (v/dot (v/->Vector 3 5) (v/->Vector 7 9))
           (+ 21 45)))))

(testing "collinearity"
  (deftest collinear
    (is (= (v/collinear? (v/->Vector 4 -6) (v/->Vector 6 -9) (v/->Vector 2 -3))
           true))
    (is (= (v/collinear? (v/->Vector 10 20) (v/->Vector 0 0))
           true))
    (is (= (v/collinear? (v/->Vector 4 5) (v/->Vector 7 6))
           false))))

(testing "angle between two vectors"
  (deftest angle
    (is (= (util/rad-to-deg (v/angle (v/->Vector 0 1) (v/->Vector 1 0)))
           90))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 1 1) (v/->Vector 1 0)))
           45))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 1 0) (v/->Vector 1 1)))
           -45))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 1 1) (v/->Vector 1 -1)))
           90))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 1 1) (v/->Vector -1 -1)))
           180))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 1 -1) (v/->Vector 1 1)))
           -90))
    (is (= (util/rad-to-deg (v/angle (v/->Vector -1 1) (v/->Vector 1 1)))
           90))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 1 1) (v/->Vector -1 1)))
           -90))
    (is (= (util/rad-to-deg (v/angle (v/->Vector -1 -1) (v/->Vector -1 1)))
           90))
    (is (= (util/rad-to-deg (v/angle (v/->Vector -1 1) (v/->Vector -1 -1)))
           -90))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 1 5) (v/->Vector 3 7)))
           11.89))
    (is (= (util/rad-to-deg (v/angle (v/->Vector 3 7) (v/->Vector 1 5)))
           -11.89))))

(testing "projection"
  (deftest project
    (is (v/almost= (v/project (v/->Vector -1 5) (v/->Vector 4 6))
                   (v/->Vector 2 3)))
    (is (= (v/collinear?
             (v/project (v/->Vector 1 2) (v/->Vector -1 -5))
             (v/->Vector -1 -5))
           true))))

(testing "intersection: intersect function"
  (deftest intersection
    (is (= (v/intersection (v/->Vector 3 6) (v/->Vector 2 -10) (v/->Vector 2 -2) (v/->Vector 4 6)) ; intersecting
               (v/->Vector 4 1)))
    (is (= (v/intersection (v/->Vector 2 2) (v/->Vector 4 2) (v/->Vector 2 -5) (v/->Vector 1 4)) ; intersecting outside the vector
               (v/->Vector 4 3)))
    (is (= (v/intersection (v/->Vector 2 2) (v/->Vector 5 1) (v/->Vector 1 -1) (v/->Vector -5 -1)) ; collinear
               (v/->Vector Infinity Infinity)))))

(testing "intersection: intersects test"
  (deftest intersects
    (is (= (v/intersect? (v/->Vector 3 6) (v/->Vector 2 -10) (v/->Vector 2 -2) (v/->Vector 4 6)) ; intersecting
           (v/->Vector 4 1)))
    (is (= (v/intersect? (v/->Vector 2 2) (v/->Vector 4 2) (v/->Vector 2 -5) (v/->Vector 1 4)) ; intersecting outside the vector
           nil))
    (is (= (v/intersect? (v/->Vector 2 2) (v/->Vector 5 1) (v/->Vector 1 -1) (v/->Vector -5 -1)) ; collinear
           nil))))

