(ns aoc22.day01
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(comment

  (with-open [rdr (io/reader (io/resource "day01.txt"))]
    (->> (line-seq rdr)
         (partition-by string/blank?)
         (remove #(some string/blank? %))
         (map #(->> (for [s %]
                      (Long/parseLong s))
                    (reduce + 0)))
         (apply max)))

  (with-open [rdr (io/reader (io/resource "day01.txt"))]
    (->> (line-seq rdr)
         (partition-by string/blank?)
         (remove #(some string/blank? %))
         (map #(->> (for [s %]
                      (Long/parseLong s))
                    (reduce + 0)))
         (sort)
         (reverse)
         (take 3)
         (reduce + 0)))

  )

