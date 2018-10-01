(ns blackjack-clj.core
  (:require [clojure.string :refer [lower-case join]]
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

(defn deal-card
  "Deals $player a card from $deck on to $hand
  returns '(hand deck)"
  ([player deck hand]
   (println player "delt:" (cards/display-card (first deck)))
   (deal-card deck hand))
  ([deck hand]
   (list (cons (first deck) hand) (rest deck))))

(defn deal
  "Returns (deck player-hand dealer-hand)"
  []
  (let [deck (cards/shuffled-deck)
        [player-hand deck] (deal-card "You're" deck (list))
        [dealer-hand deck] (deal-card "The dealer is" deck (list))
        [player-hand deck] (deal-card "You're" deck player-hand)
        [dealer-hand deck] (do (println "The dealer is delt a card face down.")
                               (deal-card deck dealer-hand))]
    (list deck player-hand dealer-hand)))

(defn play-card
  "Plays a player hand. Returns '(balance deck player-points bet dealer?)
  If bust, bet will be zero
  dealer? represents whether the dealer plays"
  [hand deck first? balance bet]
  (let [hit    "[h]it"
        stay   "[s]tay"
        double "[d]ouble"
        options (set (list hit stay))
        options (if (and first? (>= balance bet)) (conj options double) options)
        points (cards/hand-points hand)]
    (println "Your hand:" (cards/display-hand hand))
    (cond (> points 21)
          (do (println "Bust.")
              (list balance (rest deck) points 0 false))

          (and first? (= points 21))
          (do (println "Blackjack!")
              ;; Returns:
              (list balance (rest deck) points (* 2.5 bet) false))

          :else
          (loop []
            (println "Please enter an action:" (join ", " options))
            (let [action (lower-case (read-line))]
              (cond (and (= action "h") (contains? options hit))
                    (do (println "You're delt:" (cards/display-card (first deck)))
                        (play-card (cons (first deck) hand) (rest deck) false balance bet))

                    (and (= action "s") (contains? options stay))
                    ;; Returns:
                    (list balance (rest deck) points bet true)

                    (and (= action "d") (contains? options double))
                    (do (let [hand (cons (first deck) hand)
                              points (cards/hand-points hand)]
                          (println "You're delt" (cards/display-card (first deck)))
                          (if (> (cards/hand-points hand) 21)
                            (do (println "Bust.")
                                ;; Returns:
                                (list balance (rest deck) points 0 false)))
                          ;; Returns:
                          (list (- balance bet) (rest deck) points (* 2 bet) true)))

                    :else (recur)))))))

(defn dealer-play-hand
  "Has the dealer play a hand given hand and deck
  Always hits on <soft 17
  Returns point value of hand (0 if bust)"
  [hand deck]
  (println "Dealer's hand:" (cards/display-hand hand))
  (let [points (cards/hand-points hand)]
    (cond (> points 21)
          (do (println "Dealer busts!")
              0)

          (or (< points 17) (and (= points 17) (cards/hand-soft? hand)))
          (do (println "Dealer hits.")
              (println "Dealer draws " (cards/display-card (first deck)))
              (dealer-play-hand (cons (first deck) hand) (rest deck)))

          :else
          (do (println "Dealer stays.")
              points))))

(defn play-hand
  "Walks the player through a hand of blackjack"
  [balance]
  (let [bet (loop []
              (println "Bet: ")
              ;; Note read-string is an ARBITRARY CODE EXECUTION venerability
              (let [parsed-bet (read-string (read-line))]
                (cond (not (number? parsed-bet))
                      (do
                        (println "Sorry, I didn't understand that. Please enter a number.")
                        (recur))

                      (> parsed-bet balance)
                      (do
                        (println "You can't bet more units than you have in your balanace.")
                        (recur))

                      (<= parsed-bet 0)
                      (do
                        (println "You can't bet 0 or less units")
                        (recur))

                      :else parsed-bet
                      )))
        balance (- balance bet)
        [deck player-hand dealer-hand] (deal)
        [balance deck player-points bet dealer?] (play-card player-hand deck true balance bet)]
    (if (not dealer?)
      (+ balance bet)
      (let [dealer-points (dealer-play-hand dealer-hand deck)]
        (cond
          (= dealer-points player-points)
          (do
            (println "Draw.")
            (+ balance bet))

          (< dealer-points player-points)
          (do
            (println "You win!")
            (+ balance (* 2 bet)))
          :else
          (do
            (println "House wins.")
            balance))))))

;; player bets
;; dealt 2 cards face up
;; dealer one down, one up
;; hit: always
;; stay: always
;; split: two of the same card
;; double: showing 9, 10, or 11

;; Dealer: always hits soft 17 or less, stays on hard 17 or more
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
