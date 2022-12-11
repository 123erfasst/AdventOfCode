(ns aoc22.day11
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]))

(defn find-int
  [string]
  (-> (re-find #"\d+" string)
      (Long/parseLong)))

(defn find-ints
  [string]
  (->> (re-seq #"\d+" string)
       (mapv #(Long/parseLong %))))

(defn parse-monkey
  [[monkey items op test then else]]
  {:id (find-int monkey)
   :items (find-ints items)
   :op (let [[_ rator rand] (re-find #"new = old (\S) (\S+)" op)
             f (case rator
                 "+" +
                 "*" *)]
         (if (= "old" rand)
           (fn [n] (f n n))
           (let [k (Long/parseLong rand)]
             (fn [n] (f n k)))))
   :test (find-int test)
   :then (find-int then)
   :else (find-int else)})

(defn run-rounds
  ([monkey] (run-rounds monkey 3))
  ([monkeys relief]
   (let [div (transduce (map :test) * 1 monkeys)]
     (reduce
      (fn [state {:keys [id]}]
        (let [{:keys [items op test then else]} (nth state id)]
          (reduce
           (fn [state item]
             (let [level (-> (op item)
                             (quot relief)
                             (rem div))]
               (cond-> (update-in state [id :items] pop)
                 :always (update-in [id :inspected] (fnil inc 0))
                 (zero? (rem level test)) (update-in [then :items] conj level)
                 (not (zero? (rem level test))) (update-in [else :items] conj level))))
           state
           items)))
      monkeys
      monkeys))))

(comment

  (with-open [rdr (io/reader (io/resource "day11.txt"))]
    (let [monkeys (->> (line-seq rdr)
                       (partition-all 7)
                       (mapv parse-monkey))]
      (->> (iterate run-rounds monkeys)
           (drop 20)
           (first)
           (map :inspected)
           (sort-by -)
           (take 2)
           (apply *))))

  (with-open [rdr (io/reader (io/resource "day11.txt"))]
    (let [monkeys (->> (line-seq rdr)
                       (partition-all 7)
                       (mapv parse-monkey))]
      (->> (iterate #(run-rounds % 1) monkeys)
           (drop 10000)
           (first)
           (map :inspected)
           (sort-by -)
           (take 2)
           (apply *))))

  )
