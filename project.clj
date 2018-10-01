(defproject blackjack-clj "1.0.0"
  :description "A cli blackjack game"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/math.combinatorics "0.1.4"]]
  :main blackjack-clj.core/-main
  :plugins [[cider/cider-nrepl "0.18.0"]])
