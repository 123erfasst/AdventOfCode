(ns aoc21.day06
  (:require [clojure.java.io :as io]))

(defn procreate
  [fish]
  (cond-> (into (sorted-map)
                (map (fn [[days n]] [(dec days) n]))
                (subseq fish > 0))
    (contains? fish 0)
    (-> (update 6 (fnil + 0) (get fish 0))
        (assoc 8 (get fish 0)))))

(comment
  (with-open [rdr (io/reader (io/resource "day06.txt"))]
    (let [fish (->> (slurp rdr)
                    (re-seq #"\d+")
                    (map #(Long/parseLong %))
                    (frequencies)
                    (into (sorted-map)))]
      (-> (iterate procreate fish)
          (nth 80)
          (->> (vals) (apply +)))))

  (with-open [rdr (io/reader (io/resource "day06.txt"))]
    (let [fish (->> (slurp rdr)
                    (re-seq #"\d+")
                    (map #(Long/parseLong %))
                    (frequencies)
                    (into (sorted-map)))]
      (-> (iterate procreate fish)
          (nth 256)
          (->> (vals) (apply +))))))
