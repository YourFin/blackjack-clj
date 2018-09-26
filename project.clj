(defproject blackjack-clj "0.1.0-SNAPSHOT"
  :description "A cli blackjack game"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/math.combinatorics "0.1.4"]]
  :main blackjack-clj.core/-main
  :plugins [[cider/cider-nrepl "0.18.0"]])
