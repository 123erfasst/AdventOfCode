(ns aoc22.day03
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn priority
  [ch]
  (if (Character/isUpperCase ch)
    (+ 27 (- (int ch) (int \A)))
    (+ 1 (- (int ch) (int \a)))))


(comment

  (with-open [rdr (io/reader (io/resource "day03.txt"))]
    (->> (line-seq rdr)
         (mapcat #(->> (split-at (quot (count %) 2) %)
                       (map set)
                       (apply set/intersection)))
         (map priority)
         (apply +)))

  (with-open [rdr (io/reader (io/resource "day03.txt"))]
    (->> (line-seq rdr)
         (partition-all 3)
         (mapcat #(->> (map set %)
                       (apply set/intersection)))
         (map priority)
         (apply +)))

  )


