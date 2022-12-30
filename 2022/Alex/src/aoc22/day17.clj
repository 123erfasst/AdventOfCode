(ns aoc22.day17
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(def shapes
  [[[0 0] [1 0] [2 0] [3 0]]
   [[1 0] [0 1] [1 1] [2 1] [1 2]]
   [[0 0] [1 0] [2 0] [2 1] [2 2]]
   [[0 0] [0 1] [0 2] [0 3]]
   [[0 0] [1 0] [0 1] [1 1]]])

(defn intersects?
  [tower rock]
  (boolean (some #(contains? tower %) rock)))

(defn fall
  [tower rock]
  (let [new-rock (for [pos rock]
                   (mapv + pos [0 -1]))]
    (when (not (intersects? tower new-rock))
      new-rock)))

(defn push
  [tower rock dir]
  (let [offset (case dir
                 :left  [-1 0]
                 :right [ 1 0])
        new-rock (for [pos rock]
                   (mapv + pos offset))]
    (when (and (every? (fn [[x y]] (<= 0 x 6)) new-rock)
               (not (intersects? tower new-rock)))
      new-rock)))

(defn step
  [state dir]
  (let [{:keys [tower max-height stopped rock rocks]} state
        pushed (or (push tower rock dir) rock)
        dropped (fall tower pushed)]
    (if (some? dropped)
      {:tower tower
       :max-height max-height
       :stopped stopped
       :rocks rocks
       :rock dropped}
      (let [new-max-height (->> (map second rock)
                                (apply max max-height))]
        {:tower (into tower pushed)
         :max-height new-max-height
         :stopped (inc stopped)
         :rocks (next rocks)
         :rock (for [pos (first rocks)]
                 (mapv + pos [2 (+ 4 new-max-height)]))}))))

(comment

  (with-open [rdr (io/reader (io/resource "day17.txt"))]
    (let [ops (cycle (for [ch (first (line-seq rdr))]
                       (case ch
                         \< :left
                         \> :right)))]
      (->> (reductions
            step
            {:tower (into #{} (map #(vector % -1)) (range 7))
             :max-height 0
             :stopped 0
             :rocks (next (cycle shapes))
             :rock (for [pos (first shapes)]
                     (mapv + pos [2 3]))}
            ops)
           (drop-while #(< (:stopped %) 2022))
           (map :max-height)
           (first)
           (inc))))

  (with-open [rdr (io/reader (io/resource "day17.txt"))]
    (let [line (first (line-seq rdr))
          ops (cycle (for [ch line]
                       (case ch
                         \< :left
                         \> :right)))
          {:keys [stopped-delta
                  stopped-offset
                  max-height-delta
                  max-height-offset]}
          (->> (reductions
                step
                {:tower (into #{} (map #(vector % -1)) (range 7))
                 :max-height 0
                 :stopped 0
                 :rocks (next (cycle shapes))
                 :rock (for [pos (first shapes)]
                         (mapv + pos [2 3]))}
                ops)
               (map-indexed
                (fn [dirs-applied {:keys [tower max-height rock stopped]}]
                  (let [relief (->> (group-by first tower)
                                    (sort)
                                    (map (fn [[_ xs]]
                                           (apply max (map second xs))))
                                    (partition 2 1)
                                    (mapv (fn [[a b]] (- b a))))
                        dir-delta (rem dirs-applied (count line))]
                    {:relief relief
                     :max-height max-height
                     :stopped stopped
                     :rock (mapv (fn [pos] (mapv - pos [0 max-height])) rock)
                     :dir-delta dir-delta})))
               (partition-by :relief)
               (map first)
               (reduce
                (fn [seen {:keys [dir-delta max-height stopped relief rock]}]
                  (if-some [[m s] (seen [dir-delta relief rock])]
                    (reduced
                     {:stopped-delta (- stopped s)
                      :stopped-offset s
                      :max-height-delta (- max-height m)
                      :max-height-offset m
                      :relief relief
                      :rock rock
                      :dir-delta dir-delta})
                    (assoc seen [dir-delta relief rock] [max-height stopped])))
                {}))]
      (->> (reductions
            step
            {:tower (into #{} (map #(vector % -1)) (range 7))
             :max-height 0
             :stopped 0
             :rocks (next (cycle shapes))
             :rock (for [pos (first shapes)]
                     (mapv + pos [2 3]))}
            ops)
           (drop-while #(< (:stopped %) (+ stopped-offset (rem (- 1000000000000 stopped-offset) stopped-delta))))
           (map :max-height)
           (first)
           (- max-height-offset)
           (-)
           (inc)
           (+ max-height-offset (* max-height-delta (quot (- 1000000000000 stopped-offset) stopped-delta))))))

  )
