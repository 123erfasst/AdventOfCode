(ns aoc21.day16
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.walk :as walk]))

(def parse-table
  {\0 "0000"
   \1 "0001"
   \2 "0010"
   \3 "0011"
   \4 "0100"
   \5 "0101"
   \6 "0110"
   \7 "0111"
   \8 "1000"
   \9 "1001"
   \A "1010"
   \B "1011"
   \C "1100"
   \D "1101"
   \E "1110"
   \F "1111"})

(defn parse-binary
  [string]
  (Long/parseLong string 2))

(defn parse-header
  [bits]
  {:version (parse-binary (subs bits 0 3))
   :type-id (parse-binary (subs bits 3 6))})

(defn parse-literal
  [bits]
  (let [length (->> (range 0 (count bits) 5)
                    (filter #(= \0 (nth bits %)))
                    (first)
                    (+ 5))]
    {:size length
     :value (->> (range 0 length 5)
                 (map #(subs bits (inc %) (+ 5 %)))
                 (apply str)
                 (parse-binary))}))

(declare parse-packet)

(defn parse-operator
  [bits]
  (let [length-type-id (first bits)
        bits (subs bits 1)
        packets (case length-type-id
                  \0 (let [cnt (parse-binary (subs bits 0 15))
                           bits (subs bits 15)]
                       (->> (iterate (fn [{:keys [bits packets] :as state}]
                                       (if (seq bits)
                                         (let [packet (parse-packet bits)]
                                           {:bits (subs bits (:size packet))
                                            :packets (conj packets packet)})
                                         state))
                                     {:bits (subs bits 0 cnt)
                                      :packets []})
                            (drop-while #(< (apply + (map :size (:packets %))) cnt))
                            (first)
                            (:packets)))
                  \1 (let [cnt (parse-binary (subs bits 0 11))
                           bits (subs bits 11)]
                       (->> (iterate (fn [{:keys [bits packets]}]
                                       (let [packet (parse-packet bits)]
                                         {:bits (subs bits (:size packet))
                                          :packets (conj packets packet)}))
                                     {:bits bits
                                      :packets []})
                            (drop-while #(< (count (:packets %)) cnt))
                            (first)
                            (:packets)
                            (take cnt))))
        len (case length-type-id
              \0 15
              \1 11)]
    {:size (apply + 1 len (map :size packets))
     :value packets}))

(defn parse-packet
  [bits]
  (let [{:keys [version type-id]} (parse-header bits)
        {:keys [size value]} (case type-id
                               4 (parse-literal (subs bits 6))
                               (parse-operator (subs bits 6)))]
    {:version version
     :type-id type-id
     :value value
     :size (+ 6 size)}))

(comment
  (let [bits (->> (slurp (io/resource "day16.txt"))
                  (map parse-table)
                  (apply str))]
    (->> (parse-packet bits)
         (tree-seq (comp (complement #{4}) :type-id) :value)
         (map :version)
         (apply +))))

(defn interpret
  [node]
  (if (and (map? node) (:type-id node))
    (let [{:keys [type-id value]} node]
      (case type-id
        0 (apply + 0 value)
        1 (apply * 1 value)
        2 (apply min value)
        3 (apply max value)
        4 value
        5 (if (> (first value) (second value))
            1
            0)
        6 (if (< (first value) (second value))
            1
            0)
        7 (if (= (first value) (second value))
            1
            0)))
    node))

(comment
  (let [bits (->> (slurp (io/resource "day16.txt"))
                  (map parse-table)
                  (apply str))]
    (->> (parse-packet bits)
         (walk/postwalk interpret))))
