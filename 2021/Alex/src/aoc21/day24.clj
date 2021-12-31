(ns aoc21.day24
  (:require [clojure.java.io :as io]))

(defn parse-input
  [lines]
  (->> lines
       (into [] (comp (partition-all 18)
                      (map (fn [lines]
                             (let [i (case (nth lines 4)
                                       "div z 1"  15
                                       "div z 26" 5)]
                               (->> (nth lines i)
                                    (re-find #"-?\d+")
                                    (Long/parseLong)))))))
       (reduce-kv
        (fn [state i n]
          (if (neg? n)
            (let [[j m] (peek (:stack state))]
              (-> (update state :stack pop)
                  (update :bindings merge {i {:index j
                                              :delta (- (+ m n))}
                                           j {:index i
                                              :delta (+ m n)}})))
            (update state :stack conj [i n])))
        {:stack []
         :bindings (sorted-map)})
       (:bindings)))

(comment
  (with-open [rdr (io/reader (io/resource "day24.txt"))]
    (->> (parse-input (line-seq rdr))
         (reduce-kv
          (fn [acc input {:keys [index delta]}]
            (if-some [n (get acc index)]
              (assoc acc input (- n delta))
              (if (pos? delta)
                (assoc acc input (- 9 delta))
                (assoc acc input 9))))
          (sorted-map))
         (vals)
         (apply str)
         (Long/parseLong)))

  (with-open [rdr (io/reader (io/resource "day24.txt"))]
    (->> (parse-input (line-seq rdr))
         (reduce-kv
          (fn [acc input {:keys [index delta]}]
            (if-some [n (get acc index)]
              (assoc acc input (- n delta))
              (if (pos? delta)
                (assoc acc input 1)
                (assoc acc input (- 1 delta)))))
          (sorted-map))
         (vals)
         (apply str)
         (Long/parseLong))))
