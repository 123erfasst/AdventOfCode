(ns aoc21.day12
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(defn big-cave?
  [string]
  (= (s/upper-case string) string))

(def small-cave? (complement big-cave?))

(defn find-new-paths
  [state]
  (let [{:keys [graph paths result]} state]
    (->> (for [{:keys [path visited]} paths
               :let [current (peek path)]
               :when (not= "end" current)
               candidate (get graph current)
               :when (and (not= "start" candidate)
                          (or (big-cave? candidate)
                              (not (contains? visited candidate))))]
           {:path (conj path candidate)
            :visited (conj visited candidate)})
         (assoc state
                :result (into result (filter (comp #{"end"} peek :path)) paths)
                :paths))))

(comment
  (with-open [rdr (io/reader (io/resource "day12.txt"))]
    (let [graph (->> (line-seq rdr)
                     (map #(vec (re-seq #"\w+" %)))
                     (mapcat (fn [[k v]] [[k v] [v k]]))
                     (group-by first)
                     (map (fn [[k vs]] [k (set (map second vs))]))
                     (into {}))]
      (->> {:graph graph
            :paths [{:path ["start"]
                     :visited #{"start"}}]
            :result []}
           (iterate find-new-paths)
           (drop-while (comp seq :paths))
           (first)
           (:result)
           (count)))))

(defn find-new-paths2
  [state]
  (let [{:keys [graph paths result]} state]
    (->> (for [{:keys [path visited twice]} paths
               :let [current (peek path)]
               :when (not= "end" current)
               candidate (get graph current)
               :when (and (not= "start" candidate)
                          (or (big-cave? candidate)
                              (nil? twice)
                              (not (contains? visited candidate))))]
           {:path (conj path candidate)
            :visited (conj visited candidate)
            :twice (or twice
                       (when (and (contains? visited candidate)
                                  (small-cave? candidate))
                         candidate))})
         (assoc state
                :result (into result (filter (comp #{"end"} peek :path)) paths)
                :paths))))

(comment
  (with-open [rdr (io/reader (io/resource "day12.txt"))]
    (let [graph (->> (line-seq rdr)
                     (map #(vec (re-seq #"\w+" %)))
                     (mapcat (fn [[k v]] [[k v] [v k]]))
                     (group-by first)
                     (map (fn [[k vs]] [k (set (map second vs))]))
                     (into {}))]
      (->> {:graph graph
            :paths [{:path ["start"]
                     :visited #{"start"}
                     :twice nil}]
            :result []}
           (iterate find-new-paths2)
           (drop-while (comp seq :paths))
           (first)
           (:result)
           (count)))))
