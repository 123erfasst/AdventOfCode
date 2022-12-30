(ns aoc22.day22
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(comment

  (with-open [rdr (io/reader (io/resource "day22.txt"))]
    (let [[board-strings [_ ops-string]]
          (->> (line-seq rdr)
               (split-with (complement string/blank?)))
          board (->> (for [[y line] (map vector (range) board-strings)
                           [x ch] (map vector (range) line)
                           :when (not= \space ch)]
                       [[(inc x) (inc y)] (= ch \.)])
                     (into {}))
          ops (->> (re-seq #"\d+|[LR]" ops-string)
                   (mapcat (fn [s]
                             (case s
                               "L" [:left]
                               "R" [:right]
                               (repeat (Long/parseLong s) :forward)))))
          {:keys [pos dir]}
          (->> ops
               (reduce
                (fn [{:keys [pos dir] :as state} op]
                  (case op
                    :forward
                    (let [new-pos (mapv + pos dir)
                          in-front (get board new-pos)]
                      (cond
                        (nil? in-front)
                        (let [[px py] pos
                              [[x y] open] (case dir
                                             [ 1  0] (->> (filter (fn [[[x y] open]]
                                                                    (== y py))
                                                                  board)
                                                          (sort-by (fn [[[x]]] x))
                                                          (first))
                                             [-1  0] (->> (filter (fn [[[x y] open]]
                                                                    (== y py))
                                                                  board)
                                                          (sort-by (fn [[[x]]] (- x)))
                                                          (first))
                                             [ 0  1] (->> (filter (fn [[[x y] open]]
                                                                    (== x px))
                                                                  board)
                                                          (sort-by (fn [[[_ y]]] y))
                                                          (first))
                                             [ 0 -1] (->> (filter (fn [[[x y] open]]
                                                                    (== x px))
                                                                  board)
                                                          (sort-by (fn [[[_ y]]] (- y)))
                                                          (first)))]
                          (if (true? open)
                            (assoc state :pos [x y])
                            state))
                        (true? in-front) {:pos new-pos :dir dir}
                        :else state))
                    :left (->> (case dir
                                 [ 1  0] [ 0 -1]
                                 [-1  0] [ 0  1]
                                 [ 0  1] [ 1  0]
                                 [ 0 -1] [-1  0])
                               (assoc state :dir))
                    :right (->> (case dir
                                  [ 1  0] [ 0  1]
                                  [-1  0] [ 0 -1]
                                  [ 0  1] [-1  0]
                                  [ 0 -1] [ 1  0])
                                (assoc state :dir))))
                {:pos (->> (filter (fn [[[_ y] open]]
                                     (and (== 1 y) (true? open)))
                                   board)
                           (sort-by (fn [[[x]]] x))
                           (ffirst))
                 :dir [1 0]}))
          [x y] pos]
      (+ (* 1000 y) (* 4 x)
         (case dir
           [ 1  0] 0
           [-1  0] 2
           [ 0  1] 1
           [ 0 -1] 3))))

  (with-open [rdr (io/reader (io/resource "day22.txt"))]
    (let [[board-strings [_ ops-string]]
          (->> (line-seq rdr)
               (split-with (complement string/blank?)))
          dim (-> (string/join "" board-strings)
                  (string/replace #"\s" "")
                  (count)
                  (/ 6)
                  (Math/sqrt)
                  (int))
          boards (->> (partition dim board-strings)
                      (apply map vector)
                      (map #(string/replace (string/join "" %) #"\s+" ""))
                      (map #(partition dim %))
                      (apply map vector)
                      (map (fn [i b]
                             {:id (inc i)
                              :board (->> (for [[y row] (map vector (range) b)
                                                [x ch] (map vector (range) row)]
                                            [[(inc x) (inc y)] (= ch \.)])
                                          (into {}))})
                           (range))
                      (into {} (map (juxt :id identity))))
          ops (->> (re-seq #"\d+|[LR]" ops-string)
                   (mapcat (fn [s]
                             (case s
                               "L" [:left]
                               "R" [:right]
                               (repeat (Long/parseLong s) :forward)))))
          edges {[1 :left] [4 :left]
                 [1 :right] [2 :left]
                 [5 :left] [4 :right]
                 [5 :right] [2 :right]
                 [6 :top] [4 :bottom]
                 [6 :right] [5 :bottom]
                 [6 :left] [1 :top]
                 [6 :bottom] [2 :top]
                 [3 :bottom] [5 :top] 
                 [3 :top] [1 :bottom]
                 [3 :left] [4 :top]
                 [3 :right] [2 :bottom]}
          wrap-around (merge edges (set/map-invert edges))
          {:keys [board pos dir]}
          (->> ops
               (reduce
                (fn [{:keys [board pos dir] :as state} op]
                  (case op
                    :forward
                    (let [new-pos (mapv + pos dir)
                          in-front (get-in boards [board :board new-pos])]
                      (cond
                        (nil? in-front)
                        (let [[px py] pos
                              k (case dir
                                  [ 1  0] :right
                                  [-1  0] :left
                                  [ 0  1] :bottom
                                  [ 0 -1] :top)
                              [new-board new-k] (get wrap-around [board k])
                              new-dir (case new-k
                                        :left   [ 1  0]
                                        :right  [-1  0]
                                        :top    [ 0  1]
                                        :bottom [ 0 -1])
                              [px py] (case [k new-k]
                                        [:left :top]      [py 1]
                                        [:left :bottom]   [(inc (- dim py)) dim]
                                        [:right :top]     [(inc (- dim py)) 1]
                                        [:right :bottom]  [py dim]
                                        [:top :left]      [1 px]
                                        [:top :right]     [dim (inc (- dim px))]
                                        [:bottom :left]   [1 (inc (- dim px))]
                                        [:bottom :right]  [dim px]
                                        [:left :left]     [1 (inc (- dim py))]
                                        [:right :right]   [dim (inc (- dim py))]
                                        [:top :top]       [(inc (- dim px)) 1]
                                        [:bottom :bottom] [(inc (- dim px)) dim]
                                        [:left :right] [dim py]
                                        [:right :left] [1 py]
                                        [:top :bottom] [px dim]
                                        [:bottom :top] [px 1]
                                        )
                              [[x y] open]
                              (case new-dir
                                [ 1  0] (->> (filter (fn [[[x y] open]]
                                                       (== y py))
                                                     (get-in boards [new-board :board]))
                                             (sort-by (fn [[[x]]] x))
                                             (first))
                                [-1  0] (->> (filter (fn [[[x y] open]]
                                                       (== y py))
                                                     (get-in boards [new-board :board]))
                                             (sort-by (fn [[[x]]] (- x)))
                                             (first))
                                [ 0  1] (->> (filter (fn [[[x y] open]]
                                                       (== x px))
                                                     (get-in boards [new-board :board]))
                                             (sort-by (fn [[[_ y]]] y))
                                             (first))
                                [ 0 -1] (->> (filter (fn [[[x y] open]]
                                                       (== x px))
                                                     (get-in boards [new-board :board]))
                                             (sort-by (fn [[[_ y]]] (- y)))
                                             (first)))]
                          (if (true? open)
                            (assoc state :pos [x y] :dir new-dir :board new-board)
                            state))
                        (true? in-front) (assoc state :pos new-pos)
                        :else state))
                    :left (->> (case dir
                                 [ 1  0] [ 0 -1]
                                 [-1  0] [ 0  1]
                                 [ 0  1] [ 1  0]
                                 [ 0 -1] [-1  0])
                               (assoc state :dir))
                    :right (->> (case dir
                                  [ 1  0] [ 0  1]
                                  [-1  0] [ 0 -1]
                                  [ 0  1] [-1  0]
                                  [ 0 -1] [ 1  0])
                                (assoc state :dir))))
                {:pos (->> (filter (fn [[[_ y] open]]
                                     (and (== 1 y) (true? open)))
                                   (get-in boards [1 :board]))
                           (sort-by (fn [[[x]]] x))
                           (ffirst))
                 :dir [1 0]
                 :board 1}))
          [x y] (->> (for [y (range 0 (* dim 6) dim)
                           x (range 0 (* dim 6) dim)
                           :let [line (nth board-strings y nil)
                                 ch (nth line x nil)]
                           :when (contains? #{\. \#} ch)]
                       [x y])
                     (drop (dec board))
                     (first)
                     (map + pos))]
      (+ (* 1000 y) (* 4 x)
         (case dir
           [ 1  0] 0
           [-1  0] 2
           [ 0  1] 1
           [ 0 -1] 3))))

  )
