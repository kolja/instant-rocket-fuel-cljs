(ns irf.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [test.irf.vector]))

(doo-tests 'test.irf.vector)
