(ns blackjack-clj.cards-test
  (:require [clojure.test :refer :all]
            [blackjack-clj.cards :refer :all]))

(def ace-hearts (list 0 hearts))
(def two-clubs (list 1 clubs))
(def king-spades (list 12 spades))
(def queen-hearts (list 11 hearts))
(def jack-clubs (list 10 clubs))
(def nine-diamonds (list 8 diamonds))

(deftest suit-test
  (testing "Suits are correctly returned by the suit function"
    (is (= hearts (suit ace-hearts)))
    (is (= clubs (suit two-clubs)))
    (is (= spades (suit king-spades)))))

(deftest display-value-test
  (testing "Names of card values are correct"
    (is (= "King" (display-value king-spades)))
    (is (= "Queen" (display-value queen-hearts)))
    (is (= "Jack" (display-value jack-clubs)))
    (is (= "Ace" (display-value ace-hearts)))
    (is (= "2" (display-value two-clubs)))))

(deftest display-card-test
  (testing "Human-readable card names are correct"
    (is (= "Ace of Hearts" (display-card ace-hearts)))
    (is (= "2 of Clubs" (display-card two-clubs)))
    (is (= "Jack of Clubs" (display-card jack-clubs)))
    (is (= "King of Spades" (display-card king-spades)))
    (is (= "9 of Diamonds" (display-card nine-diamonds)))))

(deftest hand-points-test
  (testing "Hands have their points counted correctly"
    (is (= 13 (hand-points (list ace-hearts two-clubs))))
    (is (= 12 (hand-points (list jack-clubs two-clubs))))
    (is (= 30 (hand-points (list king-spades jack-clubs queen-hearts))))
    (is (= 21 (hand-points (list jack-clubs ace-hearts))))
    (is (= 13 (hand-points (list ace-hearts two-clubs jack-clubs))))
    (is (= 13 (hand-points (list ace-hearts ace-hearts ace-hearts jack-clubs))))
    (is (= 21 (hand-points (list nine-diamonds
                                 nine-diamonds
                                 ace-hearts
                                 two-clubs))))))

(deftest hand-soft?-test
  (testing "Correctly reports hands are soft"
    (is (hand-soft? (list ace-hearts two-clubs)))
    (is (hand-soft? (list ace-hearts ace-hearts)))
    (is (hand-soft? (list jack-clubs ace-hearts))))
  (testing "Correctly reports hands are hard"
    (is (not (hand-soft? (list ace-hearts ace-hearts jack-clubs))))
    (is (not (hand-soft? (list jack-clubs nine-diamonds))))
    (is (not (hand-soft? (list two-clubs))))))
