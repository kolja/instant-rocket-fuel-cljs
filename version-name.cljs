#!/usr/local/bin/planck
(ns version-name.core (:require [clojure.string :refer [join]]))

(defn version-name []
  (let [adjectives ["awkward" "happy" "joyful" "jealous" "tiny" "jumping" "whopping" "mindless"]
        scientists ["Curie" "Lovelace" "von Neumann" "Einstein" "Schroedinger" "Turing" "Goedel" "Hilbert"]]
    (join " " (map rand-nth [adjectives scientists]))))

(print (version-name))


