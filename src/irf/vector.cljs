(ns irf.vector)

;vector.cljs
;Created by Kolja Wilcke in June 2017

(defn sqr [x] (js/Math.pow x 2))

(defn add [& args]
  "Add Vectors"
  (reduce (fn [[ax ay] [acc-x acc-y]]
            [(+ ax acc-x) (+ ay acc-y)])
          args))

(defn subtract [[ax ay] & args]
  "Subtract Vectors"
  (let [[bx by] (apply add args)]
    [(- ax bx)
     (- ay by)]))

(defn mult [[ax ay] n]
  "multiply the vector with a Number"
  [(* n ax)
   (* n ay)])

(defn div [[ax ay] n]
    "divide the vector with a Number"
   [(/ ax n)
    (/ ay n)])

(defn length-squared [[vx vy]]
    "return the length squared (for optimisation)"
    (+ (sqr vx)
       (sqr vy)))

(defn length [v]
    "returns the length of the vector (Betrag)"
    (.sqrt js/Math (length-squared v)))

(defn norm [v]
    "returns the normalized vector (Length = 1)"
    (if (= v [0 0])
      [0 0]
      (div v (length v))))

(defn dot [[ax ay] [bx by]]
    "calculate the dot product or scalar product of two vectors"
    (+ (* ax bx)
       (* ay by)))

(defn collinear? [& args]
    "determines if the given vectors are collinear"
    (let [without-null (filter #(not= % [0 0]) args)] ;; Null-Vectors are collinear with everything. Just ignore them.
      (apply = (map (fn [[x y]] (/ x y)) without-null))))

(defn angle [v1 v2]
    "returns the angle enclosed by two vectors in radians"
    (.acos js/Math (/ (dot v1 v2)
                      (* (length v1)
                         (length v2)))))

; Vector/Kreuzprodukt only exists for 3d Space by definition

(defn projectTo [v1 v2]
    "returns the component parallel to a given vector"
    (mult (/ (scalarProduct v1 v2)
             lengthSquared(v2))
          v2))


(defn intersecting [oa, a, ob, b]
    "Class method: checks if two vectors are intersecting - returns intersection point"
    (let [c (subtract ob oa)
          -b  (mult b -1)
          col [a -b c]
          m (/ (a :y) (a :x))
          mu (/ (- (c :y) (* m (c :x)))
                (- (-b :y) (* m (-b :x))))
          ]
      (subtract ob
                (mult -b mu))))

