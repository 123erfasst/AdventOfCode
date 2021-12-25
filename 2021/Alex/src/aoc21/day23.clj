(ns aoc21.day23
  (:require [clojure.java.io :as io]))

(def get-cost {3 1 5 10 7 100 9 1000})

(defn room-to-hall
  [state]
  (let [{:keys [rooms hall cost room-size]} state]
    (for [[pos room] rooms
          :when (some #(not= pos %) room)
          :let [top (peek room)]
          [hall-pos] (concat (->> (rsubseq hall < pos)
                                  (take-while (comp nil? val)))
                             (->> (subseq hall > pos)
                                  (take-while (comp nil? val))))
          :let [move-cost (* (get-cost top)
                             (+ (Math/abs (- hall-pos pos))
                                (inc (- room-size (count room)))))]]
      {:hall (assoc hall hall-pos top)
       :rooms (update rooms pos pop)
       :cost (+ cost move-cost)
       :room-size room-size})))

(defn hall-to-room
  [state]
  (let [{:keys [hall rooms room-size]} state]
    (->> (for [[hall-pos amphipod] hall
               :when (and (some? amphipod)
                          (every? #(= amphipod %) (get rooms amphipod))
                          (every? nil? (vals (if (< hall-pos amphipod)
                                               (subseq hall > hall-pos < amphipod)
                                               (subseq hall > amphipod < hall-pos)))))]
           [hall-pos amphipod])
         (reduce
          (fn [state [hall-pos amphipod]]
            (let [{:keys [rooms]} state
                  room (get rooms amphipod)
                  move-cost (* (get-cost amphipod)
                               (+ (Math/abs (- hall-pos amphipod))
                                  (- room-size (count room))))]
              (-> state
                  (assoc-in [:hall hall-pos] nil)
                  (update-in [:rooms amphipod] conj amphipod)
                  (update :cost + move-cost))))
          state))))

(defn room-to-room
  [state]
  (let [{:keys [hall rooms room-size]} state]
    (->> (for [[pos room] rooms
               :let [top (peek room)]
               :when (and (some? top) (not= top pos))
               :let [other-room (get rooms top)]
               :when (and (< (count other-room) room-size)
                          (every? #{top} other-room)
                          (every? nil? (vals (if (< top pos)
                                               (subseq hall > top < pos)
                                               (subseq hall > pos < top)))))]
           [pos top])
         (reduce
          (fn [state [from to]]
            (let [{:keys [rooms]} state
                  move-cost (* (get-cost to)
                               (+ (Math/abs (- from to))
                                  (inc (- room-size (count (get rooms from))))
                                  (- room-size (count (get rooms to)))))]
              (-> state
                  (update-in [:rooms from] pop)
                  (update-in [:rooms to] conj to)
                  (update :cost + move-cost))))
          state))))

(defn step
  [{:keys [queue min-cost]}]
  (let [[k states] (first queue)
        {:keys [hall rooms cost room-size] :as state} (first states)
        done? (fn [{:keys [rooms]}]
                (every? (fn [[pos room]]
                          (and (= room-size (count room))
                               (apply = pos room)))
                        rooms))
        new-queue (if (< 1 (count states))
                    (update queue k disj state)
                    (dissoc queue k))]
    (if (<= min-cost cost)
      {:queue new-queue :min-cost min-cost}
      (let [new-states (->> (room-to-hall state)
                            (map (fn [state]
                                   (->> (iterate (comp hall-to-room room-to-room) state)
                                        (partition 2 1)
                                        (drop-while (fn [[a b]] (not= a b)))
                                        (ffirst)))))
            new-min-cost (transduce
                          (comp (filter done?)
                                (map :cost))
                          min
                          min-cost
                          new-states)]
        {:queue (reduce
                 (fn [queue state]
                   (let [new-k (->> (for [[pos room] (:rooms state)
                                          amphipod (take-while #{pos} room)]
                                      (get-cost amphipod))
                                    (apply - (:cost state)))]
                     (update queue new-k (fnil conj #{}) state)))
                 new-queue
                 (remove done? new-states))
         :min-cost new-min-cost}))))

(defn min-cost
  [rooms]
  (let [get-cost {3 1 5 10 7 100 9 1000}
        room-size (count (first (vals rooms)))]
    (->> {:queue (sorted-map
                  (->> (for [[pos room] rooms
                             amphipod (take-while #{pos} room)]
                         (get-cost amphipod))
                       (apply - 0))
                  [{:rooms rooms
                    :hall (into (sorted-map) (zipmap [1 2 4 6 8 10 11] (repeat nil)))
                    :cost 0
                    :room-size room-size}])
          :min-cost Long/MAX_VALUE}
         (iterate step)
         (drop-while (comp seq :queue))
         (first)
         (:min-cost))))

(comment
  (->> (slurp (io/resource "day23.txt"))
       (re-seq #"\w+")
       (map {"A" 3 "B" 5 "C" 7 "D" 9})
       (partition 4)
       (apply map (comp vec rseq vector))
       (zipmap [3 5 7 9])
       (min-cost))

  (with-open [rdr (io/reader (io/resource "day23.txt"))]
    (let [[top bottom] (split-at 3 (line-seq rdr))]
      (->> (concat top
                   ["  #D#C#B#A#"
                    "  #D#B#A#C#"]
                   bottom)
           (apply str)
           (re-seq #"\w+")
           (map {"A" 3 "B" 5 "C" 7 "D" 9})
           (partition 4)
           (apply map (comp vec rseq vector))
           (zipmap [3 5 7 9])
           (min-cost)))))
