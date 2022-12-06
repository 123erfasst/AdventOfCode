(ns aoc22.day05
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn parse
  [lines]
  (let [stacks (->> (butlast (take-while (complement string/blank?) lines))
                    (map #(->> (partition-all 4 %)
                               (map (comp string/trim (partial apply str)))
                               (map (fn [s] (string/replace s #"[\[\]]" "")))))
                    (apply map vector)
                    (mapv #(into [] (remove string/blank?) (rseq %)))
                    (vec))
        ops (->> (next (drop-while (complement string/blank?) lines))
                 (mapv #(for [s (re-seq #"\d+" %)]
                          (Long/parseLong s))))]
    [stacks ops]))

(comment
  
  (with-open [rdr (io/reader (io/resource "day05.txt"))]
    (let [[stacks ops] (parse (line-seq rdr))]
      (->> (reduce
            (fn [acc [n from to]]
              (let [to-move (nth acc (dec from))]
                (-> acc
                    (update (dec from) (fn [stack]
                                         (subvec stack 0 (- (count stack) n))))
                    (update (dec to) (fn [stack]
                                       (into stack (take n) (rseq (nth acc (dec from)))))))))
            stacks
            ops)
           (map peek)
           (apply str))))

  (with-open [rdr (io/reader (io/resource "day05.txt"))]
    (let [[stacks ops] (parse (line-seq rdr))]
      (->> (reduce
            (fn [acc [n from to]]
              (let [to-move (nth acc (dec from))]
                (-> acc
                    (update (dec from) (fn [stack]
                                         (subvec stack 0 (- (count stack) n))))
                    (update (dec to) (fn [stack]
                                       (let [other (nth acc (dec from))
                                             len (count other)]
                                         (into stack (subvec other (- len n) len))))))))
            stacks
            ops)
           (map peek)
           (apply str))))

  )






