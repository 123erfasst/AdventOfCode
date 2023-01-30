(ns aoc22.day18
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(defn free-surfaces
  [cubes]
  (let [by-x (vec (sort cubes))
        by-y (->> (map #(vec (take 3 (next (cycle %)))) cubes)
                  (sort)
                  (vec))
        by-z (->> (map #(vec (take 3 (drop 2 (cycle %)))) cubes)
                  (sort)
                  (vec))]
    (->> (for [sorted [by-x by-y by-z]]
           (->> (partition 2 1 sorted)
                (reduce
                 (fn [acc [[ax ay az] [bx by bz]]]
                   (cond-> acc
                     (and (== ax bx)
                          (== ay by)
                          (== 1 (Math/abs (- az bz))))
                     (+ 2)))
                 0)))
         (apply - (* 6 (count cubes))))))

(comment
  
  (with-open [rdr (io/reader (io/resource "day18.txt"))]
    (let [cubes (->> (line-seq rdr)
                     (mapv (fn [line]
                             (->> (re-seq #"\d+" line)
                                  (mapv #(Long/parseLong %))))))]
      (free-surfaces cubes)))

  (with-open [rdr (io/reader (io/resource "day18.txt"))]
    (let [cubes (->> (line-seq rdr)
                     (into #{} (map (fn [line]
                                      (->> (re-seq #"\d+" line)
                                           (mapv #(Long/parseLong %)))))))
          by-x (vec (sort cubes))
          by-y (->> (map #(vec (take 3 (next (cycle %)))) cubes)
                    (sort)
                    (vec))
          by-z (->> (map #(vec (take 3 (drop 2 (cycle %)))) cubes)
                    (sort)
                    (vec))
          [[xmin xmax]
           [ymin ymax]
           [zmin zmax]
           :as dimensions] [[(dec (ffirst by-x)) (inc (ffirst (rseq by-x)))]
                            [(dec (ffirst by-y)) (inc (ffirst (rseq by-y)))]
                            [(dec (ffirst by-z)) (inc (ffirst (rseq by-z)))]]
          expand (fn [[x y z]]
                   [[(dec x) y z]
                    [(inc x) y z]
                    [x (dec y) z]
                    [x (inc y) z]
                    [x y (dec z)]
                    [x y (inc z)]])
          inside? (fn [[x y z]]
                    (and (<= xmin x xmax)
                         (<= ymin y ymax)
                         (<= zmin z zmax)))
          step (fn [state]
                 (let [{:keys [seen more]} state]
                   (when (seq more)
                     (let [neighbors (into #{} (comp (mapcat expand)
                                                     (remove seen)
                                                     (filter inside?))
                                           more)]
                       {:seen (into seen neighbors)
                        :more neighbors}))))
          accessible (->> (iterate step {:seen cubes :more [(mapv first dimensions)]})
                          (take-while some?)
                          (last)
                          (:seen))]
      (- (free-surfaces cubes)
         (free-surfaces
          (for [z (range zmin (inc zmax))
                y (range ymin (inc ymax))
                x (range xmin (inc xmax))
                :let [cube [x y z]]
                :when (not (accessible cube))]
            cube)))))

  )
