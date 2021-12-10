(ns aoc21.day10
  (:require [clojure.java.io :as io]))

(comment
  (with-open [rdr (io/reader (io/resource "day10.txt"))]
    (let [opening {\> \<
                   \) \(
                   \] \[
                   \} \{}]
      (->> (line-seq rdr)
           (map (fn [line]
                  (reduce
                   (fn [stack ch]
                     (case ch
                       (\< \( \[ \{) (conj stack ch)
                       (\> \) \] \}) (if (= (opening ch) (peek stack))
                                       (pop stack)
                                       (reduced ch))))
                   []
                   line)))
           (filter char?)
           (map {\) 3
                 \] 57
                 \} 1197
                 \> 25137})
           (apply +))))

  (with-open [rdr (io/reader (io/resource "day10.txt"))]
    (let [opening {\> \<
                   \) \(
                   \] \[
                   \} \{}
          median (fn [scores]
                   (nth scores (quot (count scores) 2)))]
      (->> (line-seq rdr)
           (map (fn [line]
                  (reduce
                   (fn [stack ch]
                     (case ch
                       (\< \( \[ \{) (conj stack ch)
                       (\> \) \] \}) (if (= (opening ch) (peek stack))
                                       (pop stack)
                                       (reduced ch))))
                   []
                   line)))
           (filter #(and (vector? %) (seq %)))
           (map (fn [chars]
                  (transduce
                   (map {\( 1
                         \[ 2
                         \{ 3
                         \< 4})
                   (completing
                    (fn [sum ch]
                      (+ (* 5 sum) ch)))
                   0
                   (rseq chars))))
           (sort)
           (vec)
           (median)))))
