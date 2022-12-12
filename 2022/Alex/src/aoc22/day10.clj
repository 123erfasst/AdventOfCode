(ns aoc22.day10
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn parse
  [lines]
  (->> lines
       (map #(if (= "noop" %)
               :noop
               (Long/parseLong (re-find #"-?\d+" %))))
       (reductions
        (fn [{:keys [x cycle] :as state} op]
          (if (= :noop op)
            (update state :cycle inc)
            {:x (+ x op) :cycle (+ 2 cycle)}))
        {:x 1 :cycle 1})
       (into (sorted-map) (map (juxt :cycle :x)))))

(comment

  (with-open [rdr (io/reader (io/resource "day10.txt"))]
    (let [states (parse (line-seq rdr))]
      (transduce
       (comp (take 6)
             (map (fn [cycle]
                    (->> (rsubseq states <= cycle)
                         (first)
                         (val)
                         (* cycle)))))
       +
       0
       (range 20 Long/MAX_VALUE 40))))


  (with-open [rdr (io/reader (io/resource "day10.txt"))]
    (let [states (parse (line-seq rdr))]
      (->> (map #(let [x (-> (rsubseq states <= (inc %))
                             (first)
                             (val))
                       i (rem % 40)]
                   (if (<= (dec i) x (inc i))
                     "#"
                     " "))
                (range 240))
           (partition 40)
           (map #(apply str %))
           (run! println))))

  )


