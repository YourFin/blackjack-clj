(ns blackjack-clj.cards
  (:require [clojure.math.combinatorics :as combo]
            [blackjack-clj.util :refer :all]))

(def hearts "Hearts")
(def spades "Spades")
(def clubs "Clubs")
(def diamonds "Diamonds")

(defn suit
  "Returns the suit of a card"
  [[_ suit]]
  suit)

(defn display-value
  "Returns the value of the card displayed to the player"
  [[value _]]
  (cond
    (= 0 value) "Ace"
    (= 10 value) "Jack"
    (= 11 value) "Queen"
    (= 12 value) "King"
    :else (str (inc value))))

(defn display-card
  "Returns a human-readable version of the card"
  [card]
  (str (display-value card) " of " (suit card)))

(defn card-points
  "The number of points that a card counts for"
  [[value _]]
  (if (= value 0)
    11
    (min (inc value) 10)))

(defn hand-points
  "The number of points that a hand counts for"
  [cards]
  (let [[points num-aces]
        (loop [[card & rest] cards
               num-aces 0
               points-so-far 0]
          (if (nil? card)
            (list points-so-far num-aces)
            (let [points (card-points card)
                  new-sum (+ points points-so-far)]
              (if (= points 11)
                (recur rest (inc num-aces) new-sum)
                (recur rest num-aces new-sum)))))]
    ;; Cut down points if we're over 21 and have aces
    (loop [remain-aces num-aces
           remain-points points]
      (if (or (= remain-aces 0) (<= remain-points 21))
        remain-points
        (recur (dec remain-aces) (- remain-points 10))))))

(defn hand-soft?
  "Returns whether or not the hand contains an ace being
  used as 11 points"
  [hand]
  (let [points-list (map card-points hand)]
    (println (->> points-list
                  (map (fn [points] (if (= points 11) 1 points)))
                  (reduce +)
                  )
             (hand-points hand))
    (and
     (in? points-list 11)
     (not= (->> points-list
                (map (fn [points] (if (= points 11) 1 points)))
                (reduce +))
           (hand-points hand)))))

(def unshuffled-deck
  "An unshuffled deck of cards"
  (combo/cartesian-product (range 13) (list hearts spades clubs diamonds)))

(def shuffled-deck
  "returns a new shuffled deck of cards"
  (shuffle unshuffled-deck))
