(ns aoc21.day17
  (:require [clojure.java.io :as io]))

(defn y-max
  [x y1 y2]
  (->> (for [dy (range (inc x))
             y (->> (iterate dec dy)
                    (reductions + 0)
                    (drop-while #(< y1 %))
                    (take-while #(<= y2 %))
                    (take 1))]
         (->> (iterate dec dy)
              (reductions + 0)
              (take-while #(<= y2 %))
              (apply max)))
       (apply max)))

(comment
  (let [[x1 x2 y1 y2] (->> (slurp (io/resource "day17.txt"))
                           (re-seq #"-?\d+")
                           (map #(Long/parseLong %)))]
    (y-max x2 y2 y1))

  (let [[x1 x2 y1 y2] (->> (slurp (io/resource "day17.txt"))
                           (re-seq #"-?\d+")
                           (map #(Long/parseLong %)))
        ys (->> (for [dy (range (min y1 y2) (inc x2))
                      steps (->> (iterate dec dy)
                                 (reductions + 0)
                                 (map vector (range))
                                 (drop-while (fn [[i y]] (< (max y1 y2) y)))
                                 (take-while (fn [[i y]] (<= (min y1 y2) y)))
                                 (map first))]
                  [steps dy])
                (group-by first)
                (into {} (map (fn [[k vs]] [k (into #{} (map second) vs)]))))
        xs (->> (for [dx (range (inc x2))
                      steps (->> (iterate #(cond
                                             (zero? %) %
                                             (pos? %) (dec %)
                                             :else (inc %))
                                          dx)
                                 (reductions + 0)
                                 (take (inc (apply max (keys ys))))
                                 (map vector (range))
                                 (drop-while (fn [[i x]] (< x x1)))
                                 (take-while (fn [[i x]] (<= x x2)))
                                 (map first))]
                  [steps dx])
                (group-by first)
                (into {} (map (fn [[k vs]] [k (into #{} (map second) vs)]))))]
    (->> (for [[steps dxs] xs
               dx dxs
               dy (get ys steps)]
           [dx dy])
         (distinct)
         (count))))
