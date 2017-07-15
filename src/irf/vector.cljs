(ns irf.vector)

;vector.cljs
;by Kolja Wilcke in June 2017

(defrecord Vector [x y])

(defn sqr [x] (js/Math.pow x 2))
(def abs js/Math.abs)
(def atan2 js/Math.atan2)
(def pi (.-PI js/Math))
(def v0 (->Vector 0 0)) ;; null-vektor

(defn quadrant [{:keys [x y]}]
  "which quadrant does a vector point to"
  (case [(pos? x) (pos? y)]
    [true true] 1
    [true false] 2
    [false false] 3
    [false true] 4))

(defn pitch [{:keys [x y]}]
  "pitch angle (aka angle of climb/slope)"
  (/ y x))

(defmulti almost= #(cond (number? %) :number
                         (instance? irf.vector/Vector %) :vector))
(defmethod almost= :number
  [& args]
  "approximate equality for numbers"
  (let [mx (apply max args)
        mn (apply min args)
        e (/ (max (abs mn) (abs mx)) 1e10)]
    (< (- mx mn) e)))

(defmethod almost= :vector
  [& args]
  "approximate equality for vectors"
  (and (apply almost= (map :x args))
       (apply almost= (map :y args))))

(defn add [& args]
  "Add Vectors"
  (reduce (fn [{x :x y :y} {acc-x :x acc-y :y}]
            (->Vector (+ x acc-x)
                      (+ y acc-y)))
          args))

(defn subtract [{ax :x ay :y} & args]
  "Subtract Vectors"
  (let [{bx :x by :y} (apply add args)]
    (->Vector (- ax bx)
              (- ay by))))

(defn mult [{:keys [x y]} n]
  "multiply the vector with a Number"
  (->Vector (* n x)
            (* n y)))

(defn div [{:keys [x y]} n]
    "divide the vector with a Number"
    (->Vector (/ x n)
              (/ y n)))

(defn length-squared [{:keys [x y]}]
    "return the length squared (for optimisation)"
    (+ (sqr x)
       (sqr y)))

(defn length [a]
    "returns the length of the vector (Betrag)"
    (.sqrt js/Math (length-squared a)))

(defn norm [a]
    "returns the normalized vector (Length = 1)"
    (if (= a v0)
      a
      (div a (length a))))

(defn dot [{ax :x ay :y} {bx :x by :y}]
    "calculate the dot product or scalar product of two vectors"
    (+ (* ax bx)
       (* ay by)))

(defn collinear? [& args]
    "determines if the given vectors are collinear"
    (let [without-null (remove #(= % v0) args)] ;; Null-Vectors are collinear with everything. Just ignore them.
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

(defn project [a b]
    "returns the component parallel to a given vector"
    (let [u (norm b)]
      (mult u (dot a u))))

(defn intersection [pa {ax :x ay :y} pb b]
    "Calculates the intersection point of two lines
    defined by vectors a and b and their position vectors (pa and pb)."
    (let [{cx :x cy :y}   (subtract pb pa)
          {-bx :x -by :y :as -b} (mult b -1)
          m (/ ay ax)
          mu (/ (- cy (* m cx))
                (- -by (* m -bx)))]
      (subtract pb (mult -b mu))))

(defn intersect? [pa a pb b]
  "return the intersection point (truthy) if the point lies on both a and b otherwise nil"
  (let [x (intersection pa a pb b)
        pa->x (subtract x pa)
        pb->x (subtract x pb)]
    (if (and (< (length-squared pa->x)
                (length-squared a))
             (= (quadrant pa->x) (quadrant a))
             (< (length-squared pb->x)
                (length-squared b))
             (= (quadrant pb->x) (quadrant b)))
    x
    nil)))

