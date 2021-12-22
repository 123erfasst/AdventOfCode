(ns aoc21.day22
  (:require [clojure.java.io :as io]))

(defn contains-range?
  [container range]
  (let [[c1 c2] container
        [r1 r2] range]
    (<= c1 r1 r2 c2)))

(defn contains-shape?
  [container shape]
  (let [axes (keys container)]
    (every? #(contains-range? (container %) (shape %)) axes)))

(defn intersect-range
  [a b]
  (let [[a1 a2] a
        [b1 b2] b
        i1 (max a1 b1)
        i2 (min a2 b2)]
    (when (<= i1 i2)
      [i1 i2])))

(defn intersect
  [shape-a shape-b]
  (let [axes (keys shape-a)
        is (for [axis axes]
             (intersect-range (shape-a axis) (shape-b axis)))]
    (when (every? some? is)
      (zipmap axes is))))

(defn exclude
  [shape-a shape-b]
  (when-some [i (intersect shape-a shape-b)]
    (let [{[x1 x2] :x
           [y1 y2] :y
           [z1 z2] :z} i]
      (for [xs [[Long/MIN_VALUE (dec x1)]
                [x1 x2]
                [(inc x2) Long/MAX_VALUE]]
            ys [[Long/MIN_VALUE (dec y1)]
                [y1 y2]
                [(inc y2) Long/MAX_VALUE]]
            zs [[Long/MIN_VALUE (dec z1)]
                [z1 z2]
                [(inc z2) Long/MAX_VALUE]]
            :when (not= i {:x xs :y ys :z zs})
            :let [i2 (intersect shape-a {:x xs :y ys :z zs})]
            :when (some? i2)]
        i2))))

(defn size
  [shape]
  (->> (for [axis (keys shape)
             :let [[a b] (shape axis)]]
         (inc (Math/abs (- a b))))
       (apply *)))

(defn step
  [shapes op]
  (let [{:keys [state shape]} op
        new-shapes (for [s shapes
                         e (or (exclude s shape) [s])]
                     e)]
    (if (= :on state)
      (into [shape] new-shapes)
      (into [] new-shapes))))

(comment
  (with-open [rdr (io/reader (io/resource "day22.txt"))]
    (let [ops (for [line (line-seq rdr)
                    :let [on (re-find #"^on" line)
                          nums (re-seq #"-?\d+" line)]]
                {:state (if on :on :off)
                 :shape (->> (for [num nums]
                               (Long/parseLong num))
                             (into [] (partition-all 2))
                             (zipmap [:x :y :z]))})
          container {:x [-50 50] :y [-50 50] :z [-50 50]}]
      (->> ops
           (filter #(contains-shape? container (:shape %)))
           (reduce step [])
           (map size)
           (apply +))))

  (with-open [rdr (io/reader (io/resource "day22.txt"))]
    (let [ops (for [line (line-seq rdr)
                    :let [on (re-find #"^on" line)
                          nums (re-seq #"-?\d+" line)]]
                {:state (if on :on :off)
                 :shape (->> (for [num nums]
                               (Long/parseLong num))
                             (into [] (partition-all 2))
                             (zipmap [:x :y :z]))})]
      (->> ops
           (reduce step [])
           (map size)
           (apply +)))))
