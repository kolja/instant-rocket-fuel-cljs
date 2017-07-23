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

(defn bb-intersect? [[{a1x :x a1y :y} {a2x :x a2y :y}]
                     [{b1x :x b1y :y} {b2x :x b2y :y}]]
  "do two bounding-boxes intersect?"
  (not (or (<= a2x b1x) (<= a2y b1y)
           (<= b2x a1x) (<= b2y a1y))))
