(ns aoc21.day19
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))

(defn read-data
  [lines]
  (let [[header & body] lines]
    {:scanner (Long/parseLong (re-find #"\d+" header))
     :positions (->> (for [line body]
                       (->> (re-seq #"-?\d+" line)
                            (mapv #(Long/parseLong %))))
                     (vec))}))

(defn vector-length
  ([v]
   (let [[x y z] v]
     (vector-length x y z)))
  ([x y z]
   (Math/sqrt (+ (* x x) (* y y) (* z z)))))

(defn normal
  ([vs]
   (let [[a b c] vs]
     (normal a b c)))
  ([a b c]
   (let [[Ux Uy Uz] (mapv - b a)
         [Vx Vy Vz] (mapv - c a)]
     [(- (* Uy Vz) (* Uz Vy))
      (- (* Uz Vx) (* Ux Vz))
      (- (* Ux Vy) (* Uy Vx))])))

(defn build-transforms
  [data]
  (->> (for [{:keys [scanner positions]} data
             :let [sorted (into (sorted-set) positions)]
             a sorted
             b (subseq sorted > a)
             c (subseq sorted > b)]
         {:scanner scanner
          :triangle [a b c]
          :lengths [(vector-length (map - a b))
                    (vector-length (map - b c))
                    (vector-length (map - c a))]})
       (group-by (comp set :lengths))
       (vals)
       (filter (comp pos? dec count))
       (reduce
        (fn [acc [a & more]]
          (if (seq more)
            (reduce
             (fn [acc b]
               (if (contains? (get acc (:scanner a)) (:scanner b))
                 acc
                 (let [triangle-a (:triangle a)
                       triangle-b (:triangle b)
                       ordered (for [l (:lengths a)
                                     :let [i (.indexOf (:lengths b) l)]]
                                 (nth triangle-b i))
                       delta-a (mapv - (first triangle-a) (second triangle-a))
                       delta-b (mapv - (first ordered) (second ordered))
                       order-ab (for [delta delta-b
                                      :let [i (.indexOf delta-a delta)
                                            j (.indexOf delta-a (- delta))]]
                                  (if (== -1 i)
                                    j
                                    i))
                       order-ba (for [delta delta-a
                                      :let [i (.indexOf delta-b delta)
                                            j (.indexOf delta-b (- delta))]]
                                  (if (== -1 i)
                                    j
                                    i))]
                   (if (some neg? order-ab)
                     acc
                     (let [N-a (normal triangle-a)
                           N-b (normal ordered)
                           dir-ab (mapv / N-b (mapv N-a order-ab))
                           dir-ba (mapv / N-a (mapv N-b order-ba))
                           offset-ab (->> (map (first triangle-a) order-ab)
                                          (map * dir-ab)
                                          (mapv - (first ordered)))
                           offset-ba (->> (map (first ordered) order-ba)
                                          (map * dir-ba)
                                          (mapv - (first triangle-a)))
                           transform-ab {:order (vec order-ab)
                                         :dir dir-ab
                                         :offset offset-ab}
                           transform-ba {:order (vec order-ba)
                                         :dir dir-ba
                                         :offset offset-ba}]
                       (-> acc
                           (assoc-in [(:scanner a) (:scanner b)] transform-ab)
                           (assoc-in [(:scanner b) (:scanner a)] transform-ba)))))))
             acc more)
            acc))
        {})))

(defn build-paths-step
  [transforms state]
  (let [{:keys [paths seen result]} state
        new-paths (for [{:keys [path seen]} paths
                        other (keys (get transforms (peek path)))
                        :when (not (contains? seen other))]
                    {:path (conj path other)
                     :seen (conj seen other)})]
    {:paths new-paths
     :seen (into seen (map (comp peek :path)) new-paths)
     :result (into result (comp (remove (comp seen peek :path))
                                (map :path)
                                (map (juxt peek (comp vec next rseq))))
                   new-paths)}))

(defn build-paths
  [transforms]
  (->> {:paths [{:seen #{0}
                 :path [0]}]
        :seen #{}
        :result {}}
       (iterate (partial build-paths-step transforms))
       (drop-while (comp seq :paths))
       (first)
       (:result)))

(comment
  (with-open [rdr (io/reader (io/resource "day19.txt"))]
    (let [data (->> (line-seq rdr)
                    (partition-by s/blank?)
                    (filter (comp seq first))
                    (mapv read-data))
          transforms (build-transforms data)
          paths (build-paths transforms)]
      (->> (for [{:keys [scanner positions]} (next data)
                 :let [path (get paths scanner)]
                 p positions]
             (->> (partition 2 1 (cons scanner path))
                  (map (fn [path] (get-in transforms path)))
                  (reduce
                   (fn [p {:keys [order dir offset]}]
                     (->> (map p order)
                          (map * dir)
                          (mapv + offset)))
                   p)))
           (concat (:positions (first data)))
           (distinct)
           (count))))

  (with-open [rdr (io/reader (io/resource "day19.txt"))]
    (let [data (->> (line-seq rdr)
                    (partition-by s/blank?)
                    (filter (comp seq first))
                    (mapv read-data))
          transforms (build-transforms data)
          paths (build-paths transforms)
          sps (->> (for [{:keys [scanner positions]} (next data)
                         :let [path (get paths scanner)
                               p (first positions)
                               p0 (->> (partition 2 1 (cons scanner path))
                                       (map (fn [path] (get-in transforms path)))
                                       (reduce
                                        (fn [p {:keys [order dir offset]}]
                                          (->> (map p order)
                                               (map * dir)
                                               (mapv + offset)))
                                        [0 0 0]))]]
                     p0)
                   (cons [0 0 0]))]
      (->> (for [sp sps
                 sp2 sps]
             (->> (map (comp #(Math/abs %) -) sp sp2)
                  (apply +)))
           (apply max)))))
