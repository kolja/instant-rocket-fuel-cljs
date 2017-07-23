(ns test.irf.poly
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [irf.vector :as v]
            [irf.poly :as p]
            [irf.util :as util]))

(enable-console-print!)

(def p1 (p/p [[2 2] [2 8] [13 8] [7 2] [3 6] [8 6]]))
(def x74 (v/->Vector 7 4))
(def x55 (v/->Vector 5 5))
(def x53 (v/->Vector 5 3))

(testing "winding number"
  (deftest winding-number
    (is (odd? (p/winding-number p1 x74)))
    (is (even? (p/winding-number p1 x55)))
    (is (even? (p/winding-number p1 x53)))))

(testing "bounding box"
  (deftest bounding-box
    (is (= [(v/v 1 1) (v/v 7 7)]
           (p/bb (p/p [[4 1] [7 4] [4 7] [1 4]])))))
  (deftest bb-intersect
    (is (p/bb-intersect? [(v/v 1 1) (v/v 7 7)]
                         [(v/v 1 1) (v/v 7 7)]))
    (is (p/bb-intersect? [(v/v 1 1) (v/v 7 7)]
                         [(v/v 3 3) (v/v 9 9)]))
    (is (not (p/bb-intersect? [(v/v 1 1) (v/v 3 3)]
                              [(v/v 0 4) (v/v 5 7)])))
    (is (not (p/bb-intersect? [(v/v 1 2) (v/v 3 7)]
                              [(v/v 4 1) (v/v 5 7)])))
    (is (not (p/bb-intersect? [(v/v 4 1) (v/v 5 7)]
                              [(v/v 1 2) (v/v 3 7)])))))


