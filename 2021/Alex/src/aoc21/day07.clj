(ns aoc21.day07
  (:require [clojure.java.io :as io]))

(comment
  (let [crabs (->> (slurp (io/resource "day07.txt"))
                   (re-seq #"\d+")
                   (map #(Long/parseLong %)))]
    (->> (range (apply min crabs)
                (inc (apply max crabs)))
         (pmap (fn [pos]
                 (->> (map #(Math/abs (- % pos)) crabs)
                      (apply +))))
         (apply min)))

  (let [crabs (->> (slurp (io/resource "day07.txt"))
                   (re-seq #"\d+")
                   (map #(Long/parseLong %))
                   (sort)
                   (vec))
        min-pos (first crabs)
        max-pos (peek crabs)
        costs (->> (range 1 (inc (- max-pos min-pos)))
                   (reductions + 0)
                   (vec))]
    (->> (range min-pos (inc max-pos))
         (pmap (fn [pos]
                 (->> (map #(nth costs (Math/abs (- % pos))) crabs)
                      (apply +))))
         (apply min))))
