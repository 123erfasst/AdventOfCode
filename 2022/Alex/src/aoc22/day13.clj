(ns aoc22.day13
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn compare-items
  [a b]
  (if (and (or (nil? a) (number? a))
           (or (nil? b) (number? b)))
    (compare a b)
    (let [c (cond-> a (number? a) (vector))
          d (cond-> b (number? b) (vector))]
      (let [s (map compare-items c d)]
        (if (every? zero? s)
          (compare (count c) (count d))
          (first (remove zero? s)))))))

(comment

  (with-open [rdr (io/reader (io/resource "day13.txt"))]
    (->> (line-seq rdr)
         (remove string/blank?)
         (map read-string)
         (partition 2)
         (keep-indexed
          (fn [i [a b]]
            (when (< (compare-items a b) 1)
              (inc i))))
         (reduce + 0)))

  (with-open [rdr (io/reader (io/resource "day13.txt"))]
    (let [more [[[6]] [[2]]]
          packets (->> (line-seq rdr)
                       (remove string/blank?)
                       (map read-string)
                       (concat more)
                       (sort compare-items))]
      (->> (map #(.indexOf packets %) more)
           (map inc)
           (apply *))))

  )
