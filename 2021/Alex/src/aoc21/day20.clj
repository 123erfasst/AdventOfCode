(ns aoc21.day20
  (:require [clojure.java.io :as io]))

(defn neighbors
  [x y]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x)      y ] [x      y ] [(inc x)      y ]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn enhance
  [state]
  (let [{:keys [algorithm background mask]} state
        next-background (if (true? background)
                          (peek algorithm)
                          (first algorithm))]
    {:background next-background
     :mask (->> (for [[x0 y0] mask
                      [x y] (neighbors x0 y0)
                      :let [i (reduce
                               (fn [bits pos]
                                 (bit-or (bit-shift-left bits 1)
                                         (if (or (and (not background) (contains? mask pos))
                                                 (and background (not (contains? mask pos))))
                                           1
                                           0)))
                               0
                               (neighbors x y))]
                      :when (not= next-background (nth algorithm i))]
                  [x y])
                (into #{}))
     :algorithm algorithm}))

(comment
  (with-open [rdr (io/reader (io/resource "day20.txt"))]
    (let [[alg _ & lines] (line-seq rdr)
          algorithm (-> (for [ch alg]
                          (case ch
                            \# true
                            \. false))
                        (vec))
          x-size (count (first lines))
          y-size (count lines)
          background false
          mask (->> (for [[y line] (map vector (range) lines)
                          [x ch] (map vector (range) line)
                          :when (= ch \#)]
                      [x y])
                    (into #{}))]
      (->> (iterate enhance {:background background
                             :mask mask
                             :algorithm algorithm})
           (drop 2)
           (first)
           (:mask)
           (count))))

  (with-open [rdr (io/reader (io/resource "day20.txt"))]
    (let [[alg _ & lines] (line-seq rdr)
          algorithm (-> (for [ch alg]
                          (case ch
                            \# true
                            \. false))
                        (vec))
          x-size (count (first lines))
          y-size (count lines)
          background false
          mask (->> (for [[y line] (map vector (range) lines)
                          [x ch] (map vector (range) line)
                          :when (= ch \#)]
                      [x y])
                    (into #{}))]
      (->> (iterate enhance {:background background
                             :mask mask
                             :algorithm algorithm})
           (drop 50)
           (first)
           (:mask)
           (count)))))
