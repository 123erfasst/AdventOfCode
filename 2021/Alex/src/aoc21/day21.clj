(ns aoc21.day21
  (:require [clojure.java.io :as io]))

(defn step
  [state]
  (let [{:keys [turn die]} state
        roll (first die)
        p (first turn)]
    (-> state
        (update p (fn [[pos score]]
                    (let [new-pos (+ pos roll)
                          new-pos (if (zero? (rem new-pos 10))
                                    10
                                    (rem new-pos 10))]
                      [new-pos (+ score new-pos)])))
        (update :turn next)
        (update :die next))))

(comment
  (with-open [rdr (io/reader (io/resource "day21.txt"))]
    (let [{p1-start 1
           p2-start 2} (->> (for [line (line-seq rdr)]
                              (->> (re-seq #"\d+" line)
                                   (mapv #(Long/parseLong %))))
                            (into {}))
          die (->> (cycle (range 1 (inc 100)))
                   (eduction
                    (partition-all 3)
                    (map #(apply + %))))
          game (->> (iterate step {:p1 [p1-start 0]
                                   :p2 [p2-start 0]
                                   :turn (cycle [:p1 :p2])
                                   :die die})
                    (reduce
                     (fn [steps state]
                       (cond
                         (<= 1000 (second (:p1 state)))
                         (reduced (* 3 steps (second (:p2 state))))
                         (<= 1000 (second (:p2 state)))
                         (reduced (* 3 steps (second (:p1 state))))
                         :else (inc steps)))
                     0))]
      game)))

(def quantum-step
  (memoize
   (fn [state]
     (let [{:keys [p1 p2 turn]} state
           [pos score] (case turn
                         :p1 p1
                         :p2 p2)]
       (->> (for [roll1 [1 2 3]
                  roll2 [1 2 3]
                  roll3 [1 2 3]
                  :let [roll (+ roll1 roll2 roll3)
                        next-pos (+ pos roll)
                        next-pos (if (zero? (rem next-pos 10))
                                   10
                                   (rem next-pos 10))
                        next-score (+ score next-pos)]]
              (if (<= 21 next-score)
                {turn 1}
                (-> state
                    (assoc turn [next-pos next-score])
                    (update :turn {:p1 :p2 :p2 :p1})
                    (quantum-step))))
            (apply merge-with +))))))

(comment
  (with-open [rdr (io/reader (io/resource "day21.txt"))]
    (let [{p1-start 1
           p2-start 2} (->> (for [line (line-seq rdr)]
                              (->> (re-seq #"\d+" line)
                                   (mapv #(Long/parseLong %))))
                            (into {}))
          {:keys [p1 p2]} (quantum-step {:p1 [p1-start 0]
                                         :p2 [p2-start 0]
                                         :turn :p1})]
      (max p1 p2))))
