(ns aoc22.day15
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn parse-line
  [line]
  (let [{:keys [sx sy bx by]
         :as item} (->> (re-seq #"-?\d+" line)
                        (map #(Long/parseLong %))
                        (zipmap [:sx :sy :bx :by]))]
    (let [dx (Math/abs (- sx bx))
          dy (Math/abs (- sy by))]
      (assoc item :size (+ dx dy)))))

(defn non-beacon-positions
  [data y ranges]
  (-> (reduce
       (fn [{:keys [pos n]} [a b]]
         (if (some? pos)
           (cond
             (<= b pos) {:pos pos :n n}
             (<= a pos) {:pos b :n (+ n (- b pos))}
             :else {:pos b :n (+ n (inc (- b a)))})
           {:pos b :n (+ n (inc (- b a)))}))
       {:pos nil :n (->> (for [{:keys [bx by]} data
                               :when (== y by)]
                           bx)
                         (distinct)
                         (count)
                         (-))}
       ranges)
      (:n)))

(defn sensor-ranges
  [data y]
  (->> data
       (keep (fn [{:keys [sx sy size]}]
               (let [dy (Math/abs (- sy y))]
                 (when (<= dy size)
                   (let [len (- size (Math/abs (- sy y)))]
                     [(- sx len) (+ sx len)])))))
       (sort)))

(defn disconnected-ranges
  [ranges]
  (reduce
   (fn [acc r]
     (if (seq acc)
       (let [[a b] (peek acc)
             [ra rb] r]
         (cond
           (<= a ra rb b) acc
           (<= a ra b) (conj (pop acc) [a rb])
           (== 1 (- ra b)) (conj (pop acc) [a rb])
           :else (conj acc r)))
       (conj acc r)))
   []
   ranges))

(comment

  (with-open [rdr (io/reader (io/resource "day15.txt"))]
    (let [y 2000000
          data (->> (line-seq rdr)
                    (map parse-line))]
      (->> (sensor-ranges data y)
           (non-beacon-positions data y))))

  ;; brute-force part2
  (with-open [rdr (io/reader (io/resource "day15.txt"))]
    (let [rmin 0
          rmax 4000000
          data (->> (line-seq rdr)
                    (map parse-line))
          [x y] (->> (for [y (range rmin rmax)
                           :let [ranges (->> (sensor-ranges data y)
                                             (map (fn [[a b :as r]]
                                                    [(max a rmin) (min b rmax)]))
                                             (disconnected-ranges))]
                           :when (< 1 (count ranges))
                           :let [[[_ x]] ranges]]
                       [(inc x) y])
                     (first))]
      (+ y (* 4000000 x))))

  )

(defn top-left
  [{:keys [sx sy size]}]
  (+ sx (- sy (inc size))))

(defn top-right
  [{:keys [sx sy size]}]
  (- (- sy (inc size)) sx))

(defn bottom-left
  [{:keys [sx sy size]}]
  (+ (* 2 (inc size)) (- (- sy (inc size)) sx)))

(defn bottom-right
  [{:keys [sx sy size]}]
  (+ (* 2 (inc size)) sx (- sy (inc size))))


(comment

  ;; efficient part2
  (with-open [rdr (io/reader (io/resource "day15.txt"))]
    (let [data (->> (line-seq rdr)
                    (map parse-line))]
      (->> (for [{ax :sx ay :sy :as a} data
                 {bx :sx by :sy :as b} (remove #{a} data)
                 :when (== (top-left a) (bottom-right b))
                 {cx :sx cy :sy :as c} (remove #{a b} data)
                 {dx :sx dy :sy :as d} (remove #{a b c} data)
                 :when (== (top-right c) (bottom-left d))
                 :let [y (/ (+ (top-left a) (top-right c)) 2)
                       x (- (top-left a) y)]
                 :when (and (<= bx x ax)
                            (<= cx x dx)
                            (<= by y ay)
                            (<= dy y cy))]
             (+ (* 4000000 x) y))
           (first))))

  )
