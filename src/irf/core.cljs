(ns irf.core
  (:require [cljs.nodejs :as nodejs]
            [irf.vector :as v]
            [irf.poly :as p]))
(nodejs/enable-util-print!)
(println "Hello from the Node!")
(def -main (fn [] nil))
(set! *main-cli-fn* -main) ;; this is required
