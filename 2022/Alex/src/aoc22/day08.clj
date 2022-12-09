(ns aoc22.day08
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn visible
  [trees]
  (->> (reductions max -1 (map first trees))
       (map (fn [[height :as tree] threshold]
              (when (< threshold height)
                tree))
            trees)
       (remove nil?)))

(comment
  
  (with-open [rdr (io/reader (io/resource "day08.txt"))]
    (let [matrix (->> (line-seq rdr)
                      (into [] (map-indexed
                                (fn [y line]
                                  (into [] (map-indexed
                                            (fn [x ch]
                                              [(Long/parseLong (str ch)) [y x]]))
                                        line)))))
          left matrix
          right (mapv rseq left)
          top (apply map vector left)
          bottom (mapv rseq top)]
      (->> (concat left right top bottom)
           (map visible)
           (apply concat)
           (distinct)
           (count))))

  )


(defn compute-visibility
  [trees]
  (let [init (vec (repeat 10 0))]
    (-> (reductions
         (fn [acc n]
           (let [k (inc n)]
             (into [] (comp cat (map inc))
                   [(subvec init 0 k)
                    (subvec acc k)])))
         init
         trees)
        (next)
        (vec))))

(comment

  (with-open [rdr (io/reader (io/resource "day08.txt"))]
    (let [matrix (->> (line-seq rdr)
                      (into [] (map-indexed
                                (fn [y line]
                                  (into [] (map-indexed
                                            (fn [x ch]
                                              (Long/parseLong (str ch))))
                                        line)))))
          left matrix
          right (mapv rseq left)
          top (apply map vector left)
          bottom (mapv rseq top)
          [l r t b] (mapv #(mapv compute-visibility %) [left right top bottom])
          len (count matrix)]
      (->> (for [y (range len)
                 x (range len)
                 :let [n (get-in matrix [y x])]]
             (->> [(get-in l [y (dec x) n])
                   (get-in r [y (- len x 2) n])
                   (get-in t [x (dec y) n])
                   (get-in b [x (- len y 2) n])]
                  (remove nil?)
                  (apply *)))
           (apply max))))

  )
