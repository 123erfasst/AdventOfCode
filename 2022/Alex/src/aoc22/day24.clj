(ns aoc22.day24
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(defn neighbors
  [pos]
  (for [dir [[1 0] [-1 0] [0 1] [0 -1]]]
    (mapv + pos dir)))

(defn trip-to
  [input]
  (let [{:keys [xmin xmax ymin ymax states initial final]} input]
    (reduce
     (fn [{:keys [positions moves]} blizzards]
       (if (contains? positions final)
         (reduced moves)
         (->> (for [pos positions
                    [xn yn :as neighbor] (cons pos (neighbors pos))
                    :when (or (= initial neighbor)
                              (= final neighbor)
                              (and (<= xmin xn xmax)
                                   (<= ymin yn ymax)
                                   (not (contains? blizzards neighbor))))]
                neighbor)
              (into #{})
              (assoc {:moves (inc moves)} :positions))))
     {:positions #{initial}
      :moves 0}
     (next states))))

(comment
  
  (with-open [rdr (io/reader (io/resource "day24.txt"))]
    (let [lines (line-seq rdr)
          xmin 1
          xmax (- (count (first lines)) 2)
          ymin 1
          ymax (- (count lines) 2)
          initial [xmin 0]
          final [xmax (inc ymax)]
          blizzards-x (->> (for [[y line] (map vector (range) lines)
                                 [x ch] (map vector (range) line)
                                 :let [dir (case ch
                                             \< :left
                                             \> :right
                                             \v :down
                                             \^ :up
                                             nil)]
                                 :when (some? dir)]
                             (case dir
                               :left  (drop (- xmax x) (cycle (reverse (map vector (range xmin (inc xmax)) (repeat y)))))
                               :up    (drop (- ymax y) (cycle (reverse (map vector (repeat x) (range ymin (inc ymax))))))
                               :right (drop (dec x)    (cycle (map vector (range xmin (inc xmax)) (repeat y))))
                               :down  (drop (dec y)    (cycle (map vector (repeat x) (range ymin (inc ymax)))))
                               nil))
                           (into [] (remove nil?)))
          states (->> (apply map hash-set blizzards-x)
                      (reduce
                       (fn [{:keys [states seen]} state]
                         (if (seen state)
                           (reduced states)
                           {:states (conj states state)
                            :seen (conj seen state)}))
                       {:states []
                        :seen #{}}))]
      (trip-to {:xmin xmin :xmax xmax
                :ymin ymin :ymax ymax
                :states (cycle states)
                :initial initial
                :final final})))

  (with-open [rdr (io/reader (io/resource "day24.txt"))]
    (let [lines (line-seq rdr)
          xmin 1
          xmax (- (count (first lines)) 2)
          ymin 1
          ymax (- (count lines) 2)
          initial [xmin 0]
          final [xmax (inc ymax)]
          blizzards-x (->> (for [[y line] (map vector (range) lines)
                                 [x ch] (map vector (range) line)
                                 :let [dir (case ch
                                             \< :left
                                             \> :right
                                             \v :down
                                             \^ :up
                                             nil)]
                                 :when (some? dir)]
                             (case dir
                               :left  (drop (- xmax x) (cycle (reverse (map vector (range xmin (inc xmax)) (repeat y)))))
                               :up    (drop (- ymax y) (cycle (reverse (map vector (repeat x) (range ymin (inc ymax))))))
                               :right (drop (dec x)    (cycle (map vector (range xmin (inc xmax)) (repeat y))))
                               :down  (drop (dec y)    (cycle (map vector (repeat x) (range ymin (inc ymax)))))
                               nil))
                           (into [] (remove nil?)))
          states (->> (apply map hash-set blizzards-x)
                      (reduce
                       (fn [{:keys [states seen]} state]
                         (if (seen state)
                           (reduced states)
                           {:states (conj states state)
                            :seen (conj seen state)}))
                       {:states []
                        :seen #{}}))
          options {:xmin xmin :xmax xmax
                   :ymin ymin :ymax ymax}
          trip-a (trip-to (merge options {:states (cycle states)
                                          :initial initial
                                          :final final}))
          trip-b (trip-to (merge options {:states (drop trip-a (cycle states))
                                          :initial final
                                          :final initial}))
          trip-c (trip-to (merge options {:states (drop (+ trip-a trip-b) (cycle states))
                                          :initial initial
                                          :final final}))]
      (+ trip-a trip-b trip-c)))

  )
