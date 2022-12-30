(ns aoc22.day19
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.pprint :refer [pprint]]))

(defn parse
  [lines]
  (mapv (fn [line]
          (let [[id ore clay-ore obsidian-ore obsidian-clay geode-ore geode-obsidian]
                (->> (re-seq #"\d+" line)
                     (map #(Long/parseLong %)))]
            {:id id
             :robots {:ore {:ore ore}
                      :clay {:ore clay-ore}
                      :obsidian {:ore obsidian-ore
                                 :clay obsidian-clay}
                      :geode {:ore geode-ore
                              :obsidian geode-obsidian}}}))
        lines))

(defn step
  [blueprint top-n states]
  (->> (for [{:keys [robots resources]} states
             :let [new-resources (merge-with + resources robots)]]
         (->> (for [[robot needed] (:robots blueprint)
                    :when (->> (merge-with - resources needed)
                               (vals)
                               (not-any? neg?))]
                {:robots (update robots robot inc)
                 :resources (merge-with - new-resources needed)})
              (cons {:robots robots :resources new-resources})))
       (apply concat)
       (distinct)
       (sort-by (fn [{:keys [robots resources]}]
                  (->> [(:geode resources)
                        (:geode robots)
                        (:obsidian resources)
                        (:obsidian robots)
                        (:clay robots)]
                       (mapv -))))
       (take top-n)))

(comment

  (with-open [rdr (io/reader (io/resource "day19.txt"))]
    (let [blueprints (->> (line-seq rdr)
                          (parse))]
      (->> (for [blueprint blueprints]
             (->> (iterate (partial step blueprint 1000)
                           [{:robots {:ore 1
                                      :clay 0
                                      :obsidian 0
                                      :geode 0}
                             :resources {:ore 0
                                         :clay 0
                                         :obsidian 0
                                         :geode 0}}])
                  (drop 24)
                  (ffirst)
                  (:resources)
                  (:geode)
                  (* (:id blueprint))))
           (apply +))))

  (with-open [rdr (io/reader (io/resource "day19.txt"))]
    (let [blueprints (->> (line-seq rdr)
                          (parse))]
      (->> (for [blueprint (take 3 blueprints)]
             (->> (iterate (partial step blueprint 10000)
                           [{:robots {:ore 1
                                      :clay 0
                                      :obsidian 0
                                      :geode 0}
                             :resources {:ore 0
                                         :clay 0
                                         :obsidian 0
                                         :geode 0}}])
                  (drop 32)
                  (ffirst)
                  (:resources)
                  (:geode)))
           (apply *))))

  )
