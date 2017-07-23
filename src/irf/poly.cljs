(ns irf.poly
  (:require [irf.vector :as v]
            [goog.string :as gstring]
            [goog.string.format]))

(defrecord Poly [vs])
(defn p [vs]
  (mapv #(apply v/->Vector %) vs))

(def pi Math.PI)
(def tau (+ pi pi))
(def round js/Math.round)
(defn deg [arc]
  (* (/ arc pi) 180))

(defn winding-number [polygon winding-pt]
  "calculates the winding number of <winding-pt>
  in relation to <polygon>. For odd numbers: winding-pt inside of polygon, even numbers: outside."
  (let
    [angles (mapv
              (fn [p1 p2]
                (let [v1 (v/subtract p1 winding-pt)
                      v2 (v/subtract p2 winding-pt)
                      a (v/angle v1 v2)]
                  a))
              polygon
              (drop 1 (conj polygon (first polygon))))]
    (round (/ (apply + angles) tau))))

(defn bb [polygon]
  "a bounding-box is a (clojure-)vector of two (irf-)vectors pointing to the lower-left/upper-right corners of a rectangle that encloses polygon"
  [(v/->Vector (apply min (map :x polygon))
               (apply min (map :y polygon)))
   (v/->Vector (apply max (map :x polygon))
               (apply max (map :y polygon)))])

(defn bb-intersect? [bb1 bb2]
  "do two bounding-boxes intersect?"
  (not (or (<=(-> bb1 first :x) (-> bb1 second :x) (-> bb2 first :x) (-> bb2 second :x))
           (<=(-> bb1 first :y) (-> bb1 second :y) (-> bb2 first :y) (-> bb2 second :y))
           (<=(-> bb2 first :y) (-> bb2 second :y) (-> bb1 first :y) (-> bb1 second :y))
           (<=(-> bb2 first :x) (-> bb2 second :x) (-> bb1 first :x) (-> bb1 second :x)))))
