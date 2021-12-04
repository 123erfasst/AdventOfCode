(ns aoc21.day01
  (:require [clojure.java.io :as io]))

(comment

  (with-open [rdr (io/reader (io/resource "day01.txt"))]
    (->> (line-seq rdr)
         (map #(Long/parseLong %))
         (partition 2 1)
         (filter (fn [[x y]] (< x y)))
         (count)))

  (with-open [rdr (io/reader (io/resource "day01.txt"))]
    (->> (line-seq rdr)
         (map #(Long/parseLong %))
         (partition 3 1)
         (map #(apply + %))
         (partition 2 1)
         (filter (fn [[x y]] (< x y)))
         (count)))
  
  )
