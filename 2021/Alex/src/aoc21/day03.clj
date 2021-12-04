(ns aoc21.day03
  (:require [clojure.java.io :as io]))

(defn parse-binary
  [string]
  (Long/parseLong string 2))

(comment
  (with-open [rdr (io/reader (io/resource "day03.txt"))]
    (let [freqs   (->> (line-seq rdr)
                       (apply map vector)
                       (map frequencies))
          gamma   (->> (map #(apply max-key val %) freqs)
                       (map key)
                       (apply str)
                       (parse-binary))
          epsilon (->> (map #(apply min-key val %) freqs)
                       (map key)
                       (apply str)
                       (parse-binary))]
      (* gamma epsilon)))

  (with-open [rdr (io/reader (io/resource "day03.txt"))]
    (let [nums (vec (line-seq rdr))
          len (count (first nums))
          f (fn [g sort-fn]
              (->> (range len)
                   (reduce
                    (fn [xs i]
                      (if (< 1 (count xs))
                        (let [bit (->> (map #(nth % i) xs)
                                       (frequencies)
                                       (sort-by sort-fn)
                                       (apply g val)
                                       (key))]
                          (filter #(= bit (nth % i)) xs))
                        (reduced xs)))
                    nums)
                   (first)))]
      (* (parse-binary (f max-key key))
         (parse-binary (f min-key (comp - int key)))))))
