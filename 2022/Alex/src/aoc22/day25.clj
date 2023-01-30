(ns aoc22.day25
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(def powers
  (for [i (range)]
    (Math/pow 5 i)))

(def mapping
  {\= -2
   \- -1
   \0  0
   \1  1
   \2  2})

(defn decode
  [s]
  (->> (map mapping s)
       (reverse)
       (map * powers)
       (apply +)
       (long)))

(defn encode
  [n]
  (let [k (->> powers
               (take-while #(< (* 2 %) n))
               (count)
               (inc))]
    (->> (take k powers)
         (reverse)
         (reduce
          (fn [{:keys [n acc]} power]
            (if (zero? n)
              {:n 0 :acc (str acc "0")}
              (if (pos? n)
                (cond
                  (< 1.5 (/ n power)) {:n (- n (* 2 power)) :acc (str acc "2")}
                  (< 0.5 (/ n power)) {:n (- n (* 1 power)) :acc (str acc "1")}
                  :else {:n n :acc (str acc "0")})
                (cond
                  (< 1.5 (/ (- n) power)) {:n (+ n (* 2 power)) :acc (str acc "=")}
                  (< 0.5 (/ (- n) power)) {:n (+ n (* 1 power)) :acc (str acc "-")}
                  :else {:n n :acc (str acc "0")}))))
          {:n n :acc ""})
         (:acc))))

(comment

  (with-open [rdr (io/reader (io/resource "day25.txt"))]
    (->> (line-seq rdr)
         (map decode)
         (apply +)
         (encode)))

  )
