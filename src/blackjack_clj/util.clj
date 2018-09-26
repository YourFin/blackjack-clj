(ns blackjack-clj.util)
(defn in?
  "Returns whether an item is in a collection"
  [coll elm]
  (some #(= elm %) coll))
