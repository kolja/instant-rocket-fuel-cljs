(ns irf.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [test.irf.vector]
              [test.irf.poly]))

(doo-tests 'test.irf.vector
           'test.irf.poly)
