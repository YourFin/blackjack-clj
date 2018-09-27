(ns blackjack-clj.core
  (:require [clojure.string :refer [lower-case]]
            [blackjack-clj.cards :as cards]))

(defn y-n
  "Prompts the user for input with $messages applied over str, followed by [Y/n]"
  [& messages]
  (println (apply str messages) "[Y/n]")
  (case (lower-case (read-line))
    "y" true
    "" true
    "n" false
    ;; Default
    (do (println "Sorry, I didn't understand that.")
        (apply y-n messages))))

;; Based on http://web.archive.org/web/20180924214815/https://www.bicyclecards.com/how-to-play/blackjack/

;; player bets
;; dealt 2 cards face up
;; dealer one down, one up
;; hit: always
;; stay: always
;; split: two of the same card
;; double: showing 9, 10, or 11

;; Dealer: always hits soft 17 or less, stays on hard 17 or more

;; Options: hit, stay, double down, split
(defn play-hand
  "Walks the player through a hand of blackjack"
  [balance]
  (loop []
    (println "Bet: ")
    ;; Note read-string is an ARBITRARY CODE EXECUTION
    ;; venerability, but this is a simple blackjack game so meh
    (let [parsed-bet (read-string (read-line))]
      (if (number? parsed-bet)
        parsed-bet
        (do
          (println "Sorry, I didn't understand that. Please enter a number.")
          (recur))))))

(defn -main
  "Main game loop, and blackjack game entry point"
  [& _]
  (loop [account-balance 1000]
    (cond (and (<= account-balance 0)
               (y-n "You appear to have run out of money."
                    "\nDo you wish to reset your balance to 1000 units?"))
          (recur 1000)

          (and (> account-balance 0)
               (y-n "Your balance is " account-balance " units. Play a hand?"))
          (recur (play-hand account-balance))

          :else (println "Game over."))))
