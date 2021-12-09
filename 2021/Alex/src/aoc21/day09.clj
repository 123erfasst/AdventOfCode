(ns aoc21.day09
  (:require [clojure.java.io :as io]))

(comment
  (with-open [rdr (io/reader (io/resource "day09.txt"))]
    (let [grid (->> (for [line (line-seq rdr)]
                      (->> (re-seq #"\d" line)
                           (mapv #(Long/parseLong %))))
                    (vec))]
      (->> (for [y (range (count grid))
                 :let [row (nth grid y)]
                 x (range (count row))
                 :let [height (nth row x)]
                 :when (and (< height (nth row (dec x) Long/MAX_VALUE))
                            (< height (nth row (inc x) Long/MAX_VALUE))
                            (< height (get-in grid [(dec y) x] Long/MAX_VALUE))
                            (< height (get-in grid [(inc y) x] Long/MAX_VALUE)))]
             (inc height))
           (apply +))))

  (with-open [rdr (io/reader (io/resource "day09.txt"))]
    (let [grid (->> (for [line (line-seq rdr)]
                      (->> (re-seq #"\d" line)
                           (mapv #(Long/parseLong %))))
                    (vec))
          low-points (->> (for [y (range (count grid))
                                :let [row (nth grid y)]
                                x (range (count row))
                                :let [height (nth row x)]
                                :when (and (< height (nth row (dec x) Long/MAX_VALUE))
                                           (< height (nth row (inc x) Long/MAX_VALUE))
                                           (< height (get-in grid [(dec y) x] Long/MAX_VALUE))
                                           (< height (get-in grid [(inc y) x] Long/MAX_VALUE)))]
                            [x y]))
          expand-basin (fn [acc]
                         (let [{:keys [points outline]} acc
                               new-points (for [point outline
                                                delta [[-1 0] [0 -1]
                                                       [ 1 0] [0  1]]
                                                :let [new-point (map + point delta)]
                                                :when (not (contains? points new-point))
                                                :let [[x y] new-point
                                                      height (get-in grid [y x] Long/MAX_VALUE)]
                                                :when (< height 9)]
                                            [x y])]
                           (if (empty? new-points)
                             (assoc acc :done true)
                             {:points (into points new-points)
                              :outline new-points})))]
      (->> (for [point low-points]
             (->> (iterate expand-basin {:points #{point}
                                         :outline [point]})
                  (drop-while (comp not :done))
                  (first)))
           (map (comp count :points))
           (sort)
           (reverse)
           (take 3)
           (apply *)))))
