(ns aoc22.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(def mapping
  {"A" :rock
   "B" :paper
   "C" :scissors
   "X" :rock
   "Y" :paper
   "Z" :scissors})

(def winning?
  #{[:rock :paper]
    [:paper :scissors]
    [:scissors :rock]})

(def losing?
  #{[:rock :scissors]
    [:paper :rock]
    [:scissors :paper]})

(def points-per-choice
  {:rock 1
   :paper 2
   :scissors 3})

(defn parse-line
  [line]
  (mapv mapping (string/split line #"\s+")))

(comment
  (with-open [rdr (io/reader (io/resource "day02.txt"))]
    (->> (line-seq rdr)
         (transduce
          (comp (map parse-line)
                (map #(+ (points-per-choice (peek %))
                         (cond
                           (winning? %) 6
                           (losing? %) 0
                           :else 3))))
          + 0))))

(def winning-choice
  {:rock :paper
   :paper :scissors
   :scissors :rock})

(def losing-choice
  (set/map-invert winning-choice))

(comment
  (with-open [rdr (io/reader (io/resource "day02.txt"))]
    (->> (line-seq rdr)
         (transduce
          (comp (map (fn [line]
                       (let [[opponent outcome] (string/split line #"\s+")]
                         [(mapping opponent) (case outcome "X" :lose "Y" :draw "Z" :win)])))
                (map (fn [[opponent outcome]]
                       (case outcome
                         :win  (+ 6 (points-per-choice (winning-choice opponent)))
                         :lose (+ 0 (points-per-choice (losing-choice opponent)))
                         :draw (+ 3 (points-per-choice opponent))))))
          + 0))))
