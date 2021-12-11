(ns aoc21.day11
  (:require [clojure.java.io :as io]))

(defn adjacent
  [i]
  (for [delta [-11 -10 -9 -1 1 9 10 11]
        :let [j (+ i delta)]
        :when (and (< -1 j 100)
                   (<= (dec (quot i 10))
                       (quot j 10)
                       (inc (quot i 10)))
                   (<= (dec (rem i 10))
                       (rem j 10)
                       (inc (rem i 10))))]
    j))

(defn step
  [state]
  (->> (:grid state)
       (into {} (map (fn [[i n]] [i (inc n)])))
       (assoc state :grid)))

(defn highlight
  [grid]
  (let [highlighted (for [[i n] grid
                          :when (< 9 n)]
                      i)
        neighbors (->> (mapcat adjacent highlighted)
                       (frequencies))]
    (reduce-kv
     (fn [grid i cnt]
       (if (zero? (get grid i))
         grid
         (update grid i + cnt)))
     (merge grid (zipmap highlighted (repeat 0)))
     neighbors)))

(defn flash
  [state]
  (let [{:keys [grid flashes]} state
        new-grid (->> (iterate highlight grid)
                      (drop-while (fn [grid] (some #(< 9 (val %)) grid)))
                      (first))]
    {:grid new-grid
     :flashes (+ flashes (count (filter zero? (vals new-grid))))}))

(comment
  (with-open [rdr (io/reader (io/resource "day11.txt"))]
    (let [nums (->> (slurp rdr)
                    (re-seq #"\d")
                    (mapv #(Long/parseLong %)))]
      (->> (iterate (comp flash step)
                    {:grid (into {} (map-indexed (fn [i n] [i n]) nums))
                     :flashes 0})
           (drop 100)
           (first)
           (:flashes))))

  (with-open [rdr (io/reader (io/resource "day11.txt"))]
    (let [nums (->> (slurp rdr)
                    (re-seq #"\d")
                    (mapv #(Long/parseLong %)))]
      (->> (iterate (comp flash step)
                    {:grid (into {} (map-indexed (fn [i n] [i n]) nums))
                     :flashes 0})
           (partition 2 1)
           (reduce
            (fn [cnt [a b]]
              (if (== 100 (- (:flashes b) (:flashes a)))
                (reduced cnt)
                (inc cnt)))
            1)))))
