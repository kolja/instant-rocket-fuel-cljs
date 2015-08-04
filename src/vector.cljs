(ns irf.vector)

;vector.cljs
;Created by Kolja Wilcke in October 2015

; null Vector
(def v0 {:x 0 :y 0})

(defn add [v1 v2]
    "Add Vectors"
    {:x (+ (v1 :x) (v2 :x))
     :y (+ (v1 :y) (v2 :y))})

(defn subtract [v1 v2]
    "Subtract Vectors"
    {:x (- (v1 :x) (v2 :x))
     :y (- (v1 :y) (v2 :y))})

(defn mult [v num]
    "multiply the vector with a Number"
    {:x (* (v :x) num)
     :y (* (v :y) num)})

(defn div [v num]
    "divide the vector with a Number"
    {:x (/ (v :x) num)
     :y (/ (v :y) num)})

(defn lengthSquared [v]
    "return the length squared (for optimisation)"
    (+ (* (v :x)(v :x)) (* (v :y) (v :y))))

(defn length [v]
    "returns the length of the vector (Betrag)"
    (.sqrt js/Math (lengthSquared v)))

(defn norm [v]
    "returns the normalized vector (Length = 1)"
    if (= v v0)
       v0
      (div v (length v)))

(defn scalarProduct [v1 v2]
    "calculate the scalar Product of two vectors"
    (+ (* (v1 :x) (v2 :x))
       (* (v1 :y) (v2 :y))))

(defn sameDirection [v1 v2]
    "determines if two vectors are collinear
     note: there may be a better way for calculating this?"
    (= (length (add v1 v2))
       (+ (length v1) (length v2))))

(defn angleWith [v1 v2]
    "returns the angle enclosed by two vectors"
    (.acos js/Math (/ (scalarProduct v1 v2)
                       (* (length v1) (length v2)))))

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

