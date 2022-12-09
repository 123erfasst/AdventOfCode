(ns aoc22.day09
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn step
  [h t]
  (let [[hx hy] h
        [dx dy :as d] (mapv - h t)]
    (case d
      [ 2  2] [(dec hx) (dec hy)] 
      [-2  2] [(inc hx) (dec hy)]
      [ 2 -2] [(dec hx) (inc hy)]
      [-2 -2] [(inc hx) (inc hy)]
      (cond
        (<  1 dx) [(dec hx) hy]
        (< dx -1) [(inc hx) hy]
        (<  1 dy) [hx (dec hy)]
        (< dy -1) [hx (inc hy)]
        :else t))))

(comment

  (with-open [rdr (io/reader (io/resource "day09.txt"))]
    (->> (line-seq rdr)
         (mapcat (fn [line]
                   (let [[d n] (string/split line #"\s+")]
                     (repeat (Long/parseLong n)
                             (case d
                               "L" [-1  0]
                               "R" [ 1  0]
                               "U" [ 0  1]
                               "D" [ 0 -1])))))
         (reductions
          (fn [[h t] dir]
            (let [h' (mapv + h dir)]
              [h' (step h' t)]))
          [[0 0] [0 0]])
         (map second)
         (distinct)
         (count)))

    (with-open [rdr (io/reader (io/resource "day09.txt"))]
    (->> (line-seq rdr)
         (mapcat (fn [line]
                   (let [[d n] (string/split line #"\s+")]
                     (repeat (Long/parseLong n)
                             (case d
                               "L" [-1  0]
                               "R" [ 1  0]
                               "U" [ 0  1]
                               "D" [ 0 -1])))))
         (reductions
          (fn [points dir]
            (->> (reductions
                  step
                  (mapv + (first points) dir)
                  (next points))
                 (vec)))
          (vec (repeat 10 [0 0])))
         (map peek)
         (distinct)
         (count)))

    )
