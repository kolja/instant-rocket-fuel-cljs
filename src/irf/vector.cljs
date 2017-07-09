(ns irf.vector)

;vector.cljs
;by Kolja Wilcke in June 2017

(defn v [x y]
  "create a new Vector with x and y components"
  {:x x :y y})

(defn sqr [x] (js/Math.pow x 2))
(def abs js/Math.abs)
(def atan2 js/Math.atan2)
(def pi (.-PI js/Math))
(def v0 (v 0 0)) ;; null-vektor

(defn quadrant [{:keys [x y]}]
  "which quadrant does a vektor point to"
  (case [(pos? x) (pos? y)]
    [true true] 1
    [true false] 2
    [false false] 3
    [false true] 4))

(defn pitch [{:keys [x y]}]
  "pitch angle (aka angle of climb/slope)"
  (/ y x))

(defn almost= [& args]
  "approximate equality for numbers"
  (let [mx (apply max args)
        mn (apply min args)
        e (/ (max (abs mn) (abs mx)) 1e10)]
    (< (- mx mn) e)))

(defn v= [& args]
  "approximate equality for vectors"
  (and (apply almost= (map :x args))
       (apply almost= (map :y args))))

(defn v== [& args]
  "strict equality for vectors"
  (and (apply = (map :x args))
       (apply = (map :y args))))

(defn add [& args]
  "Add Vectors"
  (reduce (fn [{x :x y :y} {acc-x :x acc-y :y}]
            {:x (+ x acc-x)
             :y (+ y acc-y)})
          args))

(defn subtract [{ax :x ay :y} & args]
  "Subtract Vectors"
  (let [{bx :x by :y} (apply add args)]
    {:x (- ax bx)
     :y (- ay by)}))

(defn mult [{:keys [x y]} n]
  "multiply the vector with a Number"
  {:x (* n x)
   :y (* n y)})

(defn div [{:keys [x y]} n]
    "divide the vector with a Number"
    {:x (/ x n)
     :y (/ y n)})

(defn length-squared [{:keys [x y]}]
    "return the length squared (for optimisation)"
    (+ (sqr x)
       (sqr y)))

(defn length [a]
    "returns the length of the vector (Betrag)"
    (.sqrt js/Math (length-squared a)))

(defn norm [a]
    "returns the normalized vector (Length = 1)"
    (if (v== a v0)
      a
      (div a (length a))))

(defn dot [{ax :x ay :y} {bx :x by :y}]
    "calculate the dot product or scalar product of two vectors"
    (+ (* ax bx)
       (* ay by)))

(defn collinear? [& args]
    "determines if the given vectors are collinear"
    (let [without-null (remove #(v== % v0) args)] ;; Null-Vectors are collinear with everything. Just ignore them.
      (apply almost= (map (fn [{:keys [x y]}] (/ x y)) without-null))))

;; ;; this will always return the smallest possible angle
;; (defn angle [v1 v2]
;;     "returns the angle enclosed by two vectors in radians"
;;     (.acos js/Math (/ (dot v1 v2)
;;                       (* (length v1)
;;                          (length v2)))))

(defn angle [{ax :x ay :y} {bx :x by :y}]
  "angle between two vectors in clockwise direction"
  (let [t (- (atan2 ay ax)
             (atan2 by bx))
        circle (+ pi pi)]
    (rem (+ t circle) circle))) ;; add full circle in case of negative angle

; Vector/Kreuzprodukt only exists for 3d Space by definition

(defn project [v1 v2]
    "returns the component parallel to a given vector"
    (let [u (norm v2)]
      (mult u (dot v1 u))))

(defn intersect [oa {ax :x ay :y} ob b]
    "Class method: checks if two vectors are intersecting - returns intersection point"
    (let [{cx :x cy :y}   (subtract ob oa)
          {-bx :x -by :y :as -b} (mult b -1)
          m (/ ay ax)
          mu (/ (- cy (* m cx))
                (- -by (* m -bx)))]
      (subtract ob (mult -b mu))))

