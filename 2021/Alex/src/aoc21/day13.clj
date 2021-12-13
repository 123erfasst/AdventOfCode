(ns aoc21.day13
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(defn fold-paper
  [points fold]
  (let [[axis n] fold]
    (->> (for [[x y :as p] points]
           (case axis
             :x (if (< n x)
                  [(- n (- x n)) y]
                  p)
             :y (if (< n y)
                  [x (- n (- y n))]
                  p)))
         (into #{}))))

(comment
  (with-open [rdr (io/reader (io/resource "day13.txt"))]
    (let [lines (line-seq rdr)
          points (->> lines
                      (take-while (complement s/blank?))
                      (map (fn [line]
                             (->> (re-seq #"\d+" line)
                                  (mapv #(Long/parseLong %))))))
          folds (->> lines
                     (drop (inc (count points)))
                     (map (fn [line]
                            (let [[_ axis n] (re-find #"([xy])=(\d+)" line)]
                              [(keyword axis) (Long/parseLong n)]))))]
      (count (fold-paper points (first folds)))))

  (with-open [rdr (io/reader (io/resource "day13.txt"))]
    (let [lines (line-seq rdr)
          points (->> lines
                      (take-while (complement s/blank?))
                      (map (fn [line]
                             (->> (re-seq #"\d+" line)
                                  (mapv #(Long/parseLong %))))))
          folds (->> lines
                     (drop (inc (count points)))
                     (map (fn [line]
                            (let [[_ axis n] (re-find #"([xy])=(\d+)" line)]
                              [(keyword axis) (Long/parseLong n)]))))
          paper (reduce fold-paper points folds)]
      (->> (for [y (range (inc (apply max (map second paper))))]
             (for [x (range (inc (apply max (map first paper))))]
               (if (contains? paper [x y])
                 :#
                 :.)))
           (run! prn)))))
