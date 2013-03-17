(ns ca.client.engine)

(def neighbours
  (for [i [-1 0 1]
        j [-1 0 1]
        :when (not= 0 i j)]
    [i j]))

(def nine-block
  (for [i [-1 0 1]
        j [-1 0 1]]
    [i j]))

(defn transform 
  "Transforms a point [x y] by a given offset [dx dy]"
  [[^long x ^long y] [^long dx ^long dy]]
  [(+ x dx) (+ y dy)])

(defn place [artefact position]
  (map #(transform position %) artefact))

(defn stepper [neighbours birth? survive?]
  (fn [trim-fn cells]
    (set (for [[loc n] (frequencies (mapcat neighbours cells))
               :when (and 
                       (if (cells loc) (survive? n) (birth? n))
                       (trim-fn loc))]
           loc))))

(def conways-game-of-life
  (stepper #(place neighbours %) #{3} #{2 3}))

(def semi-vote
  (stepper #(place neighbours %) #{3 5 6 7 8} #{4 6 7 8}))

(def vichniac-vote
  (stepper #(place nine-block %) #{5 6 7 8 9} #{5 6 7 8 9}))

(def unstable-vichniac-vote
  (stepper #(place nine-block %) #{4 6 7 8 9} #{4 6 7 8 9}))

(def fredkin
  (stepper #(place nine-block %) #{1 3 5 7 9} #{1 3 5 7 9}))

(def circle
  (stepper #(place neighbours %) #{3} #{1 2 4}))
