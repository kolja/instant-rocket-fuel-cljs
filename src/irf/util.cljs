(ns irf.util)


(defn rad-to-deg [rad]
  (let [pi (.-PI js/Math)]
    (js/parseFloat
      (.toFixed
        (* rad (/ 360 (* 2 pi)))
        2))))
