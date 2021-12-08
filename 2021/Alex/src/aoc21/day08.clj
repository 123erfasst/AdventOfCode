(ns aoc21.day08
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.set :as set]))

(def digit-by-pattern
  {"abcefg"  0
   "cf"      1
   "acdeg"   2
   "acdfg"   3
   "bcdf"    4
   "abdfg"   5
   "abdefg"  6
   "acf"     7
   "abcdefg" 8
   "abcdfg"  9})

(comment
  (with-open [rdr (io/reader (io/resource "day08.txt"))]
    (->> (line-seq rdr)
         (mapcat (fn [line]
                   (let [[_ xs] (s/split line #"\|")]
                     (re-seq #"\w+" xs))))
         (filter (comp #{2 3 4 7} count))
         (count)))

  (with-open [rdr (io/reader (io/resource "day08.txt"))]
    (->> (for [line (line-seq rdr)
               :let [[patterns result] (->> (re-seq #"\w+" line)
                                            (split-at 10))
                     by-count (into {} (map (juxt count set)) patterns)
                     freqs (->> (frequencies (apply concat patterns))
                                (keep (fn [[c n]]
                                        (when (contains? #{4 6 9} n)
                                          [n c])))
                                (into {}))
                     a (first (set/difference (by-count 3) (by-count 2)))
                     b (freqs 6)
                     e (freqs 4)
                     f (freqs 9)
                     c (first (disj (by-count 2) f))
                     dg (->> (filter #(= 6 (count %)) patterns)
                             (apply str)
                             (frequencies))
                     [d g] (->> (dissoc dg a b c e f)
                                (sort-by val)
                                (keys))
                     translation (zipmap [a b c d e f g] "abcdefg")]]
           (->> result
                (map #(->> (map translation %)
                           (sort)
                           (apply str)))
                (map digit-by-pattern)))
         (map (fn [[a b c d]]
                (+ (* 1000 a)
                   (* 100 b)
                   (* 10 c)
                   d)))
         (apply +))))
