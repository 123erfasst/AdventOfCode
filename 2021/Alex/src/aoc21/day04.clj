(ns aoc21.day04
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  (with-open [rdr (io/reader (io/resource "day04.txt"))]
    (let [[header & body] (->> (line-seq rdr)
                               (filter seq))
          nums (->> (s/split header #",")
                    (mapv #(Long/parseLong %)))
          boards (->> body
                      (mapcat #(s/split % #"\s+"))
                      (remove s/blank?)
                      (map #(Long/parseLong %))
                      (partition 5)
                      (partition 5)
                      (map #(into #{} (comp cat (map set))
                                  [% (apply map vector %)])))]
      (reduce
       (fn [boards n]
         (let [new-boards (for [board boards]
                            (for [xs board]
                              (disj xs n)))]
           (if-some [done (->> new-boards
                               (filter #(some empty? %))
                               (first))]
             (reduced (->> (into #{} cat done)
                           (apply +)
                           (* n)))
             new-boards)))
       boards nums)))

  (with-open [rdr (io/reader (io/resource "day04.txt"))]
    (let [[header & body] (->> (line-seq rdr)
                               (filter seq))
          nums (->> (s/split header #",")
                    (mapv #(Long/parseLong %)))
          boards (->> body
                      (mapcat #(s/split % #"\s+"))
                      (remove s/blank?)
                      (map #(Long/parseLong %))
                      (partition 5)
                      (partition 5)
                      (map #(into #{} (comp cat (map set))
                                  [% (apply map vector %)])))]
      (-> (reduce
           (fn [acc n]
             (let [new-boards (for [board (:boards acc)]
                                (for [xs board]
                                  (disj xs n)))]
               (if-some [done (->> new-boards
                                   (filter #(some empty? %))
                                   (first))]
                 (assoc acc
                        :boards (filter #(every? seq %) new-boards)
                        :winner (->> (into #{} cat done)
                                     (apply +)
                                     (* n)))
                 (assoc acc :boards new-boards))))
           {:boards boards
            :winner nil}
           nums)
          (:winner)))))
