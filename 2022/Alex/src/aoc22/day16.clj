(ns aoc22.day16
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(defn parse
  [lines]
  (->> (for [line lines
             :let [[valve rate & tunnels] (re-seq #"[A-Z]{2}|\d+" line)]]
         [valve {:valve valve
                 :rate (Long/parseLong rate)
                 :distances (zipmap tunnels (repeat 1))}])
       (into {})))

(defn propagate-distance-for
  [valves valve]
  (let [distances (get-in valves [valve :distances])]
    (if (<= (dec (count valves)) (count distances))
      valves
      (->> (for [[node distance] distances
                 :let [ds (get-in valves [node :distances])]]
             (into {} (map (fn [[k v]] [k (+ v (get ds valve))]))
                   (dissoc ds valve)))
           (apply merge-with min)
           (reduce-kv
            (fn [vs v d]
              (-> (update-in vs [v :distances valve] (fnil min Long/MAX_VALUE) d)
                  (update-in [valve :distances v] (fnil min Long/MAX_VALUE) d)))
            valves)))))

(defn propagate-distance
  [valves]
  (reduce propagate-distance-for valves (keys valves)))

(defn optimize-valves
  [valves]
  (let [vs (->> valves
                (iterate propagate-distance)
                (drop-while (fn [valves]
                              (some #(< (count (:distances %)) (dec (count valves)))
                                    (vals valves))))
                (first))
        zeros (into #{} (comp (filter #(zero? (:rate %)))
                              (map :valve))
                    (vals vs))]
    (->> (for [[k v] vs
               :when (or (= "AA" k) (not (zeros k)))]
           [k (update v :distances #(apply dissoc % zeros))])
         (into {}))))

(defn step
  [valves paths]
  (let [max-path (apply max-key :released paths)
        max-released (:released max-path)]
    (for [{:keys [valve released turns open]} paths
          [other distance] (get-in valves [valve :distances])
          :when (not (open other))
          :let [{:keys [rate]} (get valves other)
                new-turns (- turns (inc distance))
                new-released (+ released (* new-turns rate))
                new-open (conj open other)]
          :when (and (not (neg? new-turns))
                     (< max-released new-released))]
      {:valve other
       :released new-released
       :turns new-turns
       :open new-open})))

(comment
  
  (with-open [rdr (io/reader (io/resource "day16.txt"))]
    (let [valves (->> (line-seq rdr)
                      (parse)
                      (optimize-valves))]
      (->> (iterate (partial step valves) [{:valve "AA" :released 0 :turns 30 :open #{}}])
           (reduce
            (fn [prev paths]
              (if (seq paths)
                paths
                (reduced (apply max-key :released prev)))))
           (:released))))

  )

(defn step2
  [valves paths]
  (let [max-path (apply max-key :released paths)
        max-released (:released max-path)]
    (for [{:keys [valve-a valve-b released turns-a turns-b open]} paths
          [other-a distance-a] (get-in valves [valve-a :distances])
          :when (not (open other-a))
          :let [new-open (conj open other-a)]
          [other-b distance-b] (get-in valves [valve-b :distances])
          :when (not (new-open other-b))
          :let [{rate-a :rate} (get valves other-a)
                {rate-b :rate} (get valves other-b)
                new-turns-a (- turns-a (inc distance-a))
                new-turns-b (- turns-b (inc distance-b))
                new-released (+ released (* new-turns-a rate-a) (* new-turns-b rate-b))
                new-open (conj new-open other-b)]
          :when (and (not (neg? new-turns-a))
                     (not (neg? new-turns-b))
                     (< max-released new-released))]
      {:valve-a other-a
       :valve-b other-b
       :released new-released
       :turns-a new-turns-a
       :turns-b new-turns-b
       :open new-open})))

(comment
  
  (with-open [rdr (io/reader (io/resource "day16.txt"))]
    (let [valves (->> (line-seq rdr)
                      (parse)
                      (optimize-valves))]
      (->> [{:valve-a "AA"
             :valve-b "AA"
             :released 0
             :turns-a 26
             :turns-b 26
             :open #{}}]
           (iterate (partial step2 valves))
           (reduce
            (fn [prev paths]
              (if (seq paths)
                paths
                (reduced (apply max-key :released prev)))))
           (:released))))

  )

