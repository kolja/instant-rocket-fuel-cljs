(ns irf.core
  (:require [cljs.nodejs :as nodejs]
            [irf.vector :as v]))
(nodejs/enable-util-print!)
(println "Hello from the Node!")
(def -main (fn [] nil))
(set! *main-cli-fn* -main) ;; this is required
