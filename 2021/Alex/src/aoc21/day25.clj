(ns aoc21.day25
  (:require [clojure.java.io :as io]))

(with-open [rdr (io/reader (io/resource "day25.txt"))]
  (let [lines (line-seq rdr)
        size-y (count lines)
        size-x (count (first lines))
        cucumbers (set (for [[y line] (map vector (range) lines)
                             [x ch] (map vector (range) line)
                             :let [cucumber (case ch
                                              \. nil
                                              \v {:dir :south
                                                  :pos [x y]}
                                              \> {:dir :east
                                                  :pos [x y]})]
                             :when (some? cucumber)]
                         cucumber))
        step (fn [state]
               (let [{:keys [occupied cucumbers]} state
                     {:keys [east south]} (group-by :dir cucumbers)
                     state-with-east (reduce
                                      (fn [state cucumber]
                                        (let [{:keys [pos]} cucumber
                                              [x y] pos
                                              next-pos [(rem (inc x) size-x) y]]
                                          (if (contains? occupied next-pos)
                                            state
                                            (-> state
                                                (update :occupied disj pos)
                                                (update :occupied conj next-pos)
                                                (update :cucumbers disj cucumber)
                                                (update :cucumbers conj (assoc cucumber :pos next-pos))))))
                                      state
                                      east)
                     {:keys [occupied]} state-with-east]
                 (reduce
                  (fn [state cucumber]
                    (let [{:keys [pos]} cucumber
                          [x y] pos
                          next-pos [x (rem (inc y) size-y)]]
                      (if (contains? occupied next-pos)
                        state
                        (-> state
                            (update :occupied disj pos)
                            (update :occupied conj next-pos)
                            (update :cucumbers disj cucumber)
                            (update :cucumbers conj (assoc cucumber :pos next-pos))))))
                  state-with-east
                  south)))]
    (->> (iterate step {:occupied (into #{} (map :pos) cucumbers)
                        :cucumbers cucumbers})
         (partition 2 1)
         (take-while (fn [[a b]] (not= a b)))
         (count)
         (inc))))
