(ns aoc22.day04
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(comment

  (with-open [rdr (io/reader (io/resource "day04.txt"))]
    (->> (line-seq rdr)
         (map #(for [s (re-seq #"\d+" %)]
                 (Long/parseLong s)))
         (filter (fn [[a b c d]]
                   (or (<= a c d b)
                       (<= c a b d))))
         (count)))

  (with-open [rdr (io/reader (io/resource "day04.txt"))]
    (->> (line-seq rdr)
         (map #(for [s (re-seq #"\d+" %)]
                 (Long/parseLong s)))
         (remove (fn [[a b c d]]
                   (or (< b c)
                       (< d a))))
         (count)))

  )




