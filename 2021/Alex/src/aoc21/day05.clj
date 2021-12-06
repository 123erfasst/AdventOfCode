(ns aoc21.day05
  (:require [clojure.java.io :as io]))

(comment
  (with-open [rdr (io/reader (io/resource "day05.txt"))]
    (let [lines (->> (re-seq #"\d+" (slurp rdr))
                     (map #(Long/parseLong %))
                     (partition-all 4))]
      (->> (for [[x1 y1 x2 y2] lines
                 point (cond
                         (= x1 x2)
                         (for [y (if (< y1 y2)
                                   (range y1 (inc y2))
                                   (range y2 (inc y1)))]
                           [x1 y])
                         (= y1 y2)
                         (for [x (if (< x1 x2)
                                   (range x1 (inc x2))
                                   (range x2 (inc x1)))]
                           [x y1])
                         :else nil)]
             point)
           (frequencies)
           (filter #(< 1 (val %)))
           (count))))

  (with-open [rdr (io/reader (io/resource "day05.txt"))]
    (let [lines (->> (re-seq #"\d+" (slurp rdr))
                     (map #(Long/parseLong %))
                     (partition-all 4))]
      (->> (for [[x1 y1 x2 y2] lines
                 point (cond
                         (= x1 x2)
                         (for [y (if (< y1 y2)
                                   (range y1 (inc y2))
                                   (range y2 (inc y1)))]
                           [x1 y])
                         (= y1 y2)
                         (for [x (if (< x1 x2)
                                   (range x1 (inc x2))
                                   (range x2 (inc x1)))]
                           [x y1])
                         :else
                         (map vector
                              (if (< x1 x2)
                                (range x1 (inc x2))
                                (range x1 (dec x2) -1))
                              (if (< y1 y2)
                                (range y1 (inc y2))
                                (range y1 (dec y2) -1))))]
             point)
           (frequencies)
           (filter #(< 1 (val %)))
           (count)))))
