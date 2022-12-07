(ns aoc22.day06
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(comment

  (let [input (slurp (io/resource "day06.txt"))]
    (->> (partition 4 1 input)
         (reduce
          (fn [acc chars]
            (if (apply distinct? chars)
              (reduced acc)
              (inc acc)))
          4)))

  (let [input (slurp (io/resource "day06.txt"))]
    (->> (partition 14 1 input)
         (reduce
          (fn [acc chars]
            (if (apply distinct? chars)
              (reduced acc)
              (inc acc)))
          14)))

  )








