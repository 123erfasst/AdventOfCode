(ns aoc21.day15
  (:require [clojure.java.io :as io]))

(defn dijkstra
  [costs destination]
  (let [start [0 0]
        [dx dy] destination
        neighbors (fn [[x y]]
                    [[(dec x) y]
                     [(inc x) y]
                     [x (dec y)]
                     [x (inc y)]])
        step (fn [{:keys [min-costs queue]}]
               (let [pos (peek queue)
                     pos-min-cost (get min-costs pos)
                     coords (for [[x y :as coord] (neighbors pos)
                                  :when (and (<= 0 x dx)
                                             (<= 0 y dy))
                                  :let [cost (get costs coord)
                                        min-cost (get min-costs coord)
                                        new-min-cost (min (+ cost pos-min-cost) min-cost)]
                                  :when (< new-min-cost min-cost)]
                              [coord new-min-cost])]
                 {:min-costs (->> (into {} coords)
                                  (merge-with min min-costs))
                  :queue (into (pop queue) (map first) coords)}))
        {:keys [min-costs]}
        (->> (iterate step {:min-costs (-> (zipmap (keys costs) (repeat Long/MAX_VALUE))
                                           (assoc start 0))
                            :queue (conj clojure.lang.PersistentQueue/EMPTY start)})
             (drop-while (comp seq :queue))
             (first))]
    (get min-costs destination)))

(comment
  (with-open [rdr (io/reader (io/resource "day15.txt"))]
    (let [lines (line-seq rdr)
          size (count lines)
          grid (->> (for [[y line] (map vector (range) lines)
                          [x s] (map vector (range) (re-seq #"\d" line))]
                      [[x y] (Long/parseLong s)])
                    (into {}))
          destination [(dec size) (dec size)]]
      (dijkstra grid destination)))

  (with-open [rdr (io/reader (io/resource "day15.txt"))]
    (let [lines (line-seq rdr)
          size (count lines)
          values (cycle (range 1 10))
          grid (->> (for [[y line] (map vector (range) lines)
                          dy (range 5)
                          [x s] (map vector (range) (re-seq #"\d" line))
                          :let [n (Long/parseLong s)]
                          dx (range 5)
                          :let [cost (nth values (dec (+ dx dy n)))]]
                      [[(+ (* size dx) x)
                        (+ (* size dy) y)]
                       cost])
                    (into {}))
          destination [(dec (* 5 size)) (dec (* 5 size))]]
      (dijkstra grid destination))))
