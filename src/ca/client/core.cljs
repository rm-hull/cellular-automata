(ns webrot.client.conway.core
  (:require [ca.client.engine :as ca])
  (:use [monet.canvas :only [get-context fill-style rect alpha begin-path line-to move-to close-path fill]]
        [monet.core :only [animation-frame]]
        [jayq.core :only [$ document-ready data attr]]
        [jayq.util :only [log]]))

(defn trim [[^long w ^long h] [^long x ^long y]] 
  (and 
    (>= x 0) 
    (>= y 0) 
    (< x w) 
    (< y h)))

(def players
  [ ca/conways-game-of-life 
    ca/semi-vote 
    ca/fredkin 
    ;ca/circle 
    ca/vichniac-vote 
    ca/unstable-vichniac-vote ])

(def colors [:red :green :blue :yellow :purple :orange ])

;(def glider 
;  #{[2 0] [2 1] [2 2] [1 2] [0 1]})
;
;(def beacon 
;  #{[0 0] [0 1] [1 0] [1 1] [2 2] [2 3] [3 2] [3 3]})
;
;(def toad
;  #{[2 3] [2 4] [3 5] [4 2] [5 3] [5 4]})
;
;(def light-spaceship 
;  #{[2 0] [4 0] [1 1] [1 2] [1 3] [4 3] [1 4] [2 4] [3 4]})
; 
;(def oscillator
;  #{[1 0] [1 1] [1 2]})

(def seven-bar
   (set (map #(vector % 0) (range 7))))

(defn random-set [xs]
  (nth xs  (rand-int (count xs))))

(defn random-world [[w h] probability]
  (set 
    (for [x (range w)
          y (range h)
          :when (< (rand) probability)]
      [x y])))

(defn dot [ctx [x y]]
  (let [a (* x 8)
        b (* y 8)]
    (-> ctx
        (move-to a b)
        (line-to a (+ b 7))
        (line-to (+ a 7) (+ b 7))
        (line-to (+ a 7) b))))

(defn draw-cells [ctx color cells]
  (-> ctx
      (fill-style "#000000")
      (alpha 0.5)
      (rect {:x 0 :y 0 :w 800 :h 600})
      (fill-style color)
      (alpha 1.0)
      (begin-path))
  (doseq [c cells]
    (dot ctx c))
  (-> ctx
      (fill)
      (close-path)))

(defn animate [ctx cells generator-fn color]
  (letfn [(loop []
            (animation-frame loop)
            (let [next-gen (swap! cells generator-fn)]
              (draw-cells ctx color next-gen)))]  
    (draw-cells ctx color (deref cells))
    (loop)))

(defn available-area []
  (let [div (first ($ :div#wrapper))]
    [ (.-offsetWidth div) (.-offsetHeight div) ]))

(document-ready
  (fn []
    (let [canvas    ($ :canvas#world)
          ctx       (get-context (.get canvas 0) "2d")
          [w h]     (available-area)
          size      [80 60]
          threshold 0.5
          color     (random-set colors)
          trim-fn   (partial trim size)
          generator-fn (partial (random-set players) trim-fn) 
          cells     (atom (random-world size threshold))]
;      "circle" (place seven-bar [47 34]) ]
      (->
        canvas
        (attr :width w)
        (attr :height h))
      (animate ctx cells generator-fn color)   
      )))

