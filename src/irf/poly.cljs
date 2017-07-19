(ns irf.poly
  (:require [irf.vector :as v]
            [goog.string :as gstring]
            [goog.string.format]))

;; (defrecord poly [vs])

(def pi Math.PI)
(def tau (+ pi pi))
(def round js/Math.round)
(defn deg [arc]
  (* (/ arc pi) 180))

(defn winding-number [polygon winding-pt]
  (let [angles (mapv (fn [p1 p2]
                       (let [v1 (v/subtract p1 winding-pt)
                             v2 (v/subtract p2 winding-pt)
                             a (v/angle v1 v2)]
                         ;;(print (gstring/format "% d % d | % d % d -> %d" (:x v1) (:y v1) (:x v2) (:y v2) (deg a)))
                         a))
                     polygon
                     (drop 1 (conj polygon (first polygon))))]
    ;; (print (str "Winding Number: " (/ (apply + angles) tau)))
    (round (/ (apply + angles) tau))))

