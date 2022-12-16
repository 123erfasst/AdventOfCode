(ns aoc22.day14
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn parse
  [lines]
  (for [line lines
        [[ax ay] [bx by]] (->> (re-seq #"\d+" line)
                               (map #(Long/parseLong %))
                               (partition 2)
                               (partition 2 1))
        x (range (min ax bx) (inc (max ax bx)))
        y (range (min ay by) (inc (max ay by)))]
    [x y]))

(defn step
  [{:keys [by-x by-y pos] :as state}]
  (let [[x y] pos
        rpos [y x]
        [dx dy :as down] (->> (subseq by-y > rpos)
                              (keep (fn [[dy dx]]
                                      (when (== x dx)
                                        [x (dec dy)])))
                              (first))]
    (when (some? down)
      (let [left  [(dec dx) (inc dy)]
            right [(inc dx) (inc dy)]]
        (cond
          (not (contains? by-x left))  (assoc state :pos left)
          (not (contains? by-x right)) (assoc state :pos right)
          :else
          (-> (assoc state :pos [500 0])
              (update :by-x conj down)
              (update :by-y conj [dy dx])))))))

(comment

  (with-open [rdr (io/reader (io/resource "day14.txt"))]
    (let [points (parse (line-seq rdr))
          by-x (into (sorted-set) points)
          by-y (into (sorted-set) (map (comp vec rseq)) points)]
      (->> {:by-x by-x
            :by-y by-y
            :pos [500 0]}
           (iterate step)
           (take-while some?)
           (filter (comp #{[500 0]} :pos))
           (count)
           (dec))))

  (with-open [rdr (io/reader (io/resource "day14.txt"))]
    (let [points (parse (line-seq rdr))
          max-y (+ 2 (apply max (map second points)))
          more-points (map vector (range (- 500 max-y) (inc (+ 500 max-y))) (repeat max-y))
          by-x (into (sorted-set) cat [points more-points])
          by-y (into (sorted-set) (comp cat (map (comp vec rseq))) [points more-points])]
      (->> {:by-x by-x
            :by-y by-y
            :pos [500 0]}
           (iterate step)
           (take-while (fn [{:keys [by-x]}]
                         (not (contains? by-x [500 0]))))
           (filter (comp #{[500 0]} :pos))
           (count))))

  )
