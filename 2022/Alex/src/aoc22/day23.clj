(ns aoc22.day23
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(def dir-order (cycle [:north :south :west :east]))

(def north-dir [[-1 -1] [ 0 -1] [ 1 -1]])
(def south-dir [[-1  1] [ 0  1] [ 1  1]])
(def west-dir  [[-1 -1] [-1  0] [-1  1]])
(def east-dir  [[ 1 -1] [ 1  0] [ 1  1]])
(def neighbor-dirs (into #{} cat [north-dir south-dir west-dir east-dir]))

(defn free?
  [elves pos dirs]
  (->> (for [dir dirs]
         (mapv + pos dir))
       (not-any? #(contains? elves %))))

(defn step
  [state]
  (let [{:keys [elves dirs]} state
        proposed (for [pos elves
                       :when (not (free? elves pos neighbor-dirs))
                       :let [[dir & _] (for [dir (take 4 dirs)
                                             :let [d (case dir
                                                       :north north-dir
                                                       :south south-dir
                                                       :west west-dir
                                                       :east east-dir)]
                                             :when (free? elves pos d)]
                                         dir)]
                       :when (some? dir)
                       :let [new-pos (->> (case dir
                                            :north [ 0 -1]
                                            :south [ 0  1]
                                            :west  [-1  0]
                                            :east  [ 1  0])
                                          (mapv + pos))]]
                   {:pos pos :dir dir :new-pos new-pos})
        unique (->> (group-by :new-pos proposed)
                    (filter #(== 1 (count (val %))))
                    (map (comp first val)))]
    (->> (reduce
          (fn [elves {:keys [pos dir new-pos]}]
            (-> elves
                (disj pos)
                (conj new-pos)))
          elves
          unique)
         (assoc state :dirs (next dirs) :elves))))

(comment

  (with-open [rdr (io/reader (io/resource "day23.txt"))]
    (let [{:keys [elves]} (->> (for [[y line] (map vector (range) (line-seq rdr))
                                     [x ch] (map vector (range) line)
                                     :when (= \# ch)]
                                 [x y])
                               (into #{})
                               (assoc {} :dirs dir-order :elves)
                               (iterate step)
                               (drop 10)
                               (first)
                               (time))
          xmin (apply min (map first elves))
          xmax (apply max (map first elves))
          ymin (apply min (map second elves))
          ymax (apply max (map second elves))]
      (- (* (inc (- xmax xmin)) (inc (- ymax ymin))) (count elves))))

  (with-open [rdr (io/reader (io/resource "day23.txt"))]
    (->> (for [[y line] (map vector (range) (line-seq rdr))
               [x ch] (map vector (range) line)
               :when (= \# ch)]
           [x y])
         (into #{})
         (assoc {} :dirs dir-order :elves)
         (iterate step)
         (partition 2 1)
         (take-while (fn [[a b]] (not (identical? (:elves a) (:elves b)))))
         (count)
         (inc)
         (time)))

  )
