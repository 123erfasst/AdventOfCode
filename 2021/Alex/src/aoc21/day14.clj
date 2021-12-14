(ns aoc21.day14
  (:require [clojure.java.io :as io]))

(comment
  (with-open [rdr (io/reader (io/resource "day14.txt"))]
    (let [[template _ & lines] (line-seq rdr)
          insertions (->> (for [line lines
                                :let [[[a b :as pair] ch] (re-seq #"\w+" line)]]
                            [pair [(str a ch) (str ch b)]])
                          (into {}))
          step (fn [pairs]
                 (->> pairs
                      (mapcat (fn [[pair n]]
                                (for [new-pair (get insertions pair)]
                                  {new-pair n})))
                      (apply merge-with +)))
          result (->> (partition 2 1 template)
                      (map #(apply str %))
                      (frequencies)
                      (iterate step)
                      (drop 10)
                      (first)
                      (mapcat (fn [[pair n]]
                                (let [[a b] (seq pair)]
                                  [{a n} {b n}])))
                      (apply merge-with +))]
      (-> result
          (update (first template) inc)
          (update (last template) inc)
          (vals)
          (sort)
          (as-> vs (/ (- (last vs) (first vs)) 2)))))

  (with-open [rdr (io/reader (io/resource "day14.txt"))]
    (let [[template _ & lines] (line-seq rdr)
          insertions (->> (for [line lines
                                :let [[[a b :as pair] ch] (re-seq #"\w+" line)]]
                            [pair [(str a ch) (str ch b)]])
                          (into {}))
          step (fn [pairs]
                 (->> pairs
                      (mapcat (fn [[pair n]]
                                (for [new-pair (get insertions pair)]
                                  {new-pair n})))
                      (apply merge-with +)))
          result (->> (partition 2 1 template)
                      (map #(apply str %))
                      (frequencies)
                      (iterate step)
                      (drop 40)
                      (first)
                      (mapcat (fn [[pair n]]
                                (let [[a b] (seq pair)]
                                  [{a n} {b n}])))
                      (apply merge-with +))]
      (-> result
          (update (first template) inc)
          (update (last template) inc)
          (vals)
          (sort)
          (as-> vs (/ (- (last vs) (first vs)) 2))))))
