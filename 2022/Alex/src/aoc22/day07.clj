(ns aoc22.day07
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.set :as set]
            [clojure.walk :as walk]))

(defn parse
  [lines]
  (let [empty-dir {:children [] :name ""}]
    (->> (concat lines ["$ cd /"])
         (reduce
          (fn [stack line]
            (case line
              "$ cd /" (->> (rseq stack)
                            (reduce (fn [dir parent]
                                      (update parent :children conj dir)))
                            (vector))
              "$ cd .." (let [[top new-top] (rseq stack)]
                          (->> (update new-top :children conj top)
                               (conj (pop (pop stack)))))
              "$ ls" stack
              (cond
                (string/starts-with? line "dir ") stack
                (string/starts-with? line "$ cd ")
                (let [target (re-find #"\S+$" line)]
                  (conj stack {:children [] :name target}))
                :else (if-some [[_ size name] (re-find #"^(\d+) (\S+)$" line)]
                        (let [top (peek stack)]
                          (->> {:size (Long/parseLong size)
                                :name name}
                               (update top :children conj)
                               (conj (pop stack))))
                        stack))))
          [{:children []}]))))

(defn compute-size
  [tree]
  (walk/postwalk
   (fn [x]
     (if-some [children (:children x)]
       (assoc x :size (transduce (map :size) + 0 children))
       x))
   tree))

(comment
  
  (with-open [rdr (io/reader (io/resource "day07.txt"))]
    (->> (line-seq rdr)
         (parse)
         (compute-size)
         (tree-seq :children :children)
         (filter :children)
         (map :size)
         (filter #(<= % 100000))
         (reduce + 0)))

  (with-open [rdr (io/reader (io/resource "day07.txt"))]
    (let [tree (->> (line-seq rdr)
                    (parse)
                    (compute-size))
          total-size (:size tree)
          x (- total-size 40000000)]
      (->> (tree-seq :children :children tree)
           (filter :children)
           (map :size)
           (filter #(< x %))
           (sort)
           (first))))

  )
