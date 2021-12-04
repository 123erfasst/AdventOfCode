(ns aoc21.day02
  (:require [clojure.java.io :as io]))

(comment

  (with-open [rdr (io/reader (io/resource "day02.txt"))]
    (->> (line-seq rdr)
         (map (fn [line]
                (let [[_ dir amount] (re-find #"(\w+) (\d+)" line)]
                  (case dir
                    "forward" {:h (Long/parseLong amount)}
                    "up"      {:v (- (Long/parseLong amount))}
                    "down"    {:v (Long/parseLong amount)}))))
         (reduce (fn [acc data]
                   (merge-with + acc data))
                 {})
         (vals)
         (apply *)))

  (with-open [rdr (io/reader (io/resource "day02.txt"))]
    (->> (line-seq rdr)
         (map (fn [line]
                (let [[_ dir amount] (re-find #"(\w+) (\d+)" line)]
                  (case dir
                    "forward" {:h (Long/parseLong amount)}
                    "up"      {:a (- (Long/parseLong amount))}
                    "down"    {:a (Long/parseLong amount)}))))
         (reduce
          (fn [acc x]
            (let [{:keys [a h]
                   :or {h 0 a 0}} x]
              (-> acc
                  (update :h + h)
                  (update :d + (* (:a acc) h))
                  (update :a + a))))
          {:d 0
           :a 0
           :h 0})
         ((juxt :h :d))
         (apply *))))
