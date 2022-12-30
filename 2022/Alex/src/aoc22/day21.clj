(ns aoc22.day21
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(defn parse
  [lines]
  (->> (for [line lines]
         (if (re-find #"\d+" line)
           (let [[m n] (string/split line #": ")]
             {:monkey m
              :number (Long/parseLong n)})
           (let [[_ m a op b] (re-find #"(\w+): (\w+) (\S) (\w+)" line)]
             {:monkey m
              :op op
              :left a
              :right b})))
       (into {} (map (juxt :monkey identity)))))


(defn compute-constants
  [monkeys]
  (->> (tree-seq :op (juxt (comp monkeys :left) (comp monkeys :right)) (monkeys "root"))
       (reverse)
       (reduce
        (fn [constants {:keys [monkey number op left right]}]
          (if (some? number)
            (assoc constants monkey number)
            (let [l (get constants left)
                  r (get constants right)]
              (if (and l r)
                (->> (case op
                       "+" (+ l r)
                       "-" (- l r)
                       "*" (* l r)
                       "/" (/ l r))
                     (assoc constants monkey))
                constants))))
        {})))

(comment

  (with-open [rdr (io/reader (io/resource "day21.txt"))]
    (-> (line-seq rdr)
        (parse)
        (compute-constants)
        (get "root")))

  (with-open [rdr (io/reader (io/resource "day21.txt"))]
    (let [monkeys (-> (line-seq rdr)
                      (parse)
                      (dissoc "humn"))
          constants (compute-constants monkeys)
          {:keys [left right]} (get monkeys "root")
          value (some constants [left right])]
      (->> (tree-seq :op (juxt (comp monkeys :left) (comp monkeys :right)) (monkeys "root"))
           (filter :op)
           (remove #(and (contains? constants (:left %))
                         (contains? constants (:right %))))
           (next)
           (reverse)
           (transduce
            (comp)
            (fn
              ([{:keys [a b]}] (long (/ (- value b) a)))
              ([{:keys [a b]} {:keys [monkey op left right]}]
               (let [l (constants left)
                     r (constants right)
                     k (or l r)]
                 (case op
                   "+" {:a a :b (+ b k)}
                   "-" (if l
                         {:a (- a) :b (+ (- b) l)}
                         {:a a :b (- b r)})
                   "*" {:a (* a k) :b (* b k)}
                   "/" {:a (/ a k) :b (/ b k)}))))
            {:a 1 :b 0}))))

  )
