(ns aoc22.day20
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(defn mix
  [buffer input]
  (let [size (count buffer)]
    (->> (reduce
          (fn [buf item]
            (let [[_ offset] item]
              (cond
                (zero? offset) buf
                (pos? offset)
                (into [item] (comp (drop-while (complement #{item}))
                                   (remove #{item})
                                   (drop (rem offset (dec size)))
                                   (take (dec size)))
                      (cycle buf))
                :else
                (->> (into [item] (comp (drop-while (complement #{item}))
                                        (remove #{item})
                                        (drop (rem (- offset) (dec size)))
                                        (take (dec size)))
                           (cycle (rseq buf)))
                     (rseq)
                     (vec)))))
          buffer
          input)
         (cycle)
         (into [] (comp (take (* 2 size))
                        (map second)
                        (drop-while #(not (zero? %)))
                        (take size))))))

(comment

  (with-open [rdr (io/reader (io/resource "day20.txt"))]
    (let [buffer (into [] (map-indexed (fn [i n] [i (Long/parseLong n)]))
                       (line-seq rdr))
          size (count buffer)
          result (mix buffer buffer)]
      (->> [1000 2000 3000]
           (map #(nth result (rem % size)))
           (apply +))))

  (with-open [rdr (io/reader (io/resource "day20.txt"))]
    (let [k 811589153
          buffer (into [] (map-indexed (fn [i n] [i (* k (Long/parseLong n))]))
                       (line-seq rdr))
          size (count buffer)
          result (->> (repeat 10 buffer)
                      (apply concat)
                      (mix buffer))]
      (->> [1000 2000 3000]
           (map #(nth result (rem % size)))
           (apply +))))

  )
