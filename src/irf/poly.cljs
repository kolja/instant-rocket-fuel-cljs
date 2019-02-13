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

(defn closed [polygon]
  "returns a list of vertex-pairs. The last pair pairing the last vertex with the first one"
  (partition 2 1 (conj polygon (first polygon))))

(defn edges [polygon]
  "returns pairs of the vertex of a polygon and the edge
  (the edge being the difference between two vertices)."
  (map (fn [[vert1 vert2]]
         [vert1 (v/subtract vert2 vert1)])
    (closed polygon)))

(defn winding-number [polygon winding-pt]
  "calculates the winding number of <winding-pt>
  in relation to <polygon>.
  For odd numbers: winding-pt inside of polygon, even numbers: outside."
  ;; think about using reduce instead of map + apply
  (let [angles (mapv #(v/angle (v/subtract (first %) winding-pt)
                               (v/subtract (second %) winding-pt))
                     (closed polygon))]
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
  ;;(not-any? (apply <= %)
  ;;          [[a2x b1x][a2y b1y][b2x a1x][b2y a1y]])
  (not (or (<= a2x b1x) (<= a2y b1y)
           (<= b2x a1x) (<= b2y a1y))))

(defn- mark-inside [p1 p2]
  "add a flag 'inside?' to each of p1's vertices that indicates if it lies inside of p2"
  (map #(assoc % :inside? (odd? (winding-number p2 %)))
       p1))

(defn- intersections [p1 p2]
  "add vertices to p1 at the points where it intersects with p2"
  (->> (for [[p1 edge1] (edges p1)
             [p2 edge2] (edges p2)]
         (if-let [intersection (v/intersect? p1 edge1 p2 edge2)]
           [p1 intersection]
           p1))
       flatten
       (into [])))

