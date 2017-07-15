(ns test.irf.poly
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [irf.vector :as v]
            [irf.poly :as p]
            [irf.util :as util]))

(enable-console-print!)

(testing "poly creation"
  (deftest new-poly
    (is (= 2 2))))

(testing "winding number"
  (deftest winding-number
    (is (= true true))))
