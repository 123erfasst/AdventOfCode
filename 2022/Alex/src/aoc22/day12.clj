(ns aoc22.day12
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn parse
  [lines]
  (mapv (fn [line]
          (vec (for [ch line]
                 (case ch
                   \S -1
                   \E (inc (- (int \z) (int \a)))
                   (- (int ch) (int \a))))))
        lines))

(defn step
  [board left right up down paths]
  (->> (for [{:keys [pos seen height]} paths
             [v dir] [[[ 0 -1] left]
                      [[ 0  1] right]
                      [[-1  0] up]
                      [[ 1  0] down]]
             :when (get-in dir pos)
             :let [new-pos (mapv + v pos)]
             :when (not (seen new-pos))
             :let [new-height (get-in board new-pos)]
             :when (some? new-height)]
         {:pos new-pos
          :height new-height
          :seen (conj seen new-pos)})
       (group-by :pos)
       (map (comp first val))))

(defn left
  [board]
  (mapv (fn [line]
          (->> (cons -1 line)
               (partition 2 1)
               (mapv (fn [[x y]] (<= (- y x) 1)))))
        board))

(defn right
  [board]
  (mapv (fn [line]
          (->> (conj line -1)
               (partition 2 1)
               (mapv (fn [[x y]] (<= (- x y) 1)))))
        board))

(defn up
  [board]
  (->> (cons (repeat -1) board)
       (partition 2 1)
       (mapv (fn [[a b]]
               (mapv (fn [x y]
                       (<= (- y x) 1))
                     a b)))))

(defn down
  [board]
  (->> (conj board (repeat -1))
       (partition 2 1)
       (mapv (fn [[a b]]
               (mapv (fn [x y]
                       (<= (- x y) 1))
                     a b)))))

(defn find-end
  [board]
  (->> (for [y (range (count board))
             x (range (count (first board)))
             :when (== 26 (get-in board [y x]))]
         [y x])
       (first)))

(comment
  
  (with-open [rdr (io/reader (io/resource "day12.txt"))]
    (let [board (->> (line-seq rdr)
                     (parse))
          end (find-end board)]
      (->> [{:pos end :height 26 :seen #{end}}]
           (iterate (partial step board (left board) (right board) (up board) (down board)))
           (apply concat)
           (filter #(== -1 (:height %)))
           (first)
           (:seen)
           (count)
           (dec))))

  (with-open [rdr (io/reader (io/resource "day12.txt"))]
    (let [board (->> (line-seq rdr)
                     (parse))
          end (find-end board)]
      (->> [{:pos end :height 26 :seen #{end}}]
           (iterate (partial step board (left board) (right board) (up board) (down board)))
           (apply concat)
           (filter #(== 0 (:height %)))
           (first)
           (:seen)
           (count)
           (dec))))

  )
