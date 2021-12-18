(ns aoc21.day18
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.zip :as zip]))

(defn add-left
  [zipper n]
  (if-some [left (->> (iterate zip/prev zipper)
                      (next)
                      (take-while some?)
                      (take-while (complement zip/end?))
                      (filter (comp number? zip/node))
                      (first))]
    (zip/edit left + n)
    zipper))

(defn move-back
  [zipper]
  (->> (iterate zip/next zipper)
       (filter (comp #(and (number? %) (zero? %)) zip/node))
       (first)))

(defn add-right
  [zipper n]
  (if-some [right (->> (iterate zip/next zipper)
                       (next)
                       (take-while some?)
                       (take-while (complement zip/end?))
                       (filter (comp number? zip/node))
                       (first))]
    (zip/edit right + n)
    zipper))

(defn explode
  [zipper]
  (let [node (zip/node zipper)]
    (if (and (vector? node)
             (== 4 (count (zip/path zipper))))
      (let [[a b] node]
        (-> zipper
            (zip/replace 0)
            (add-left a)
            (move-back)
            (add-right b)
            (zip/root)
            (zip/vector-zip)))
      (zip/next zipper))))

(defn explode-step
  [tree]
  (->> (zip/vector-zip tree)
       (iterate explode)
       (drop-while (complement zip/end?))
       (first)
       (zip/root)))

(defn split-step
  [tree]
  (if-some [needs-split (->> (zip/vector-zip tree)
                             (iterate zip/next)
                             (take-while (complement zip/end?))
                             (filter (comp #(and (number? %) (<= 10 %)) zip/node))
                             (first))]
    (let [node (zip/node needs-split)
          half (quot node 2)]
      (-> needs-split
          (zip/replace [half (- node half)])
          (zip/root)))
    tree))

(defn reduce-number
  [number]
  (->> number
       (iterate (comp split-step explode-step))
       (partition 2 1)
       (drop-while (fn [[a b]] (not= a b)))
       (ffirst)))

(defn magnitude
  [node]
  (if (vector? node)
    (let [[a b] node]
      (+ (* 3 (magnitude a))
         (* 2 (magnitude b))))
    node))

(comment
  (with-open [rdr (io/reader (io/resource "day18.txt"))]
    (->> (line-seq rdr)
         (map edn/read-string)
         (reduce
          (fn [acc num]
            (reduce-number [acc num])))
         (magnitude)))

  (with-open [rdr (io/reader (io/resource "day18.txt"))]
    (let [numbers (->> (line-seq rdr)
                       (map edn/read-string))]
      (->> (for [a numbers
                 b numbers
                 :when (not= a b)]
             (max (magnitude (reduce-number [a b]))
                  (magnitude (reduce-number [b a]))))
           (apply max)))))
