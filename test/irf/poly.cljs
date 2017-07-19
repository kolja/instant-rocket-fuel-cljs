(ns test.irf.poly
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [irf.vector :as v]
            [irf.poly :as p]
            [irf.util :as util]))

(enable-console-print!)

(def p1 (mapv #(apply v/->Vector %) [[2 2] [2 8] [13 8] [7 2] [3 6] [8 6]]))
(def x74 (v/->Vector 7 4))
(def x55 (v/->Vector 5 5))
(def x53 (v/->Vector 5 3))

(testing "winding number"
  (deftest winding-number
    (is (odd? (p/winding-number p1 x74)))
    (is (even? (p/winding-number p1 x55)))
    (is (even? (p/winding-number p1 x53)))))

