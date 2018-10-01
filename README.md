# blackjack-clj

A clojure cli blackjack game

## Useage

#### Native:
Have [lein](https://leiningen.org/) installed.

    lein run

#### Docker:

    docker-compose build
    docker-compose run blackjack lein run

## Testing:
Have [lein](https://leiningen.org/) installed.

    lein run

## Caveats:
 - This game doesn't handle splits
 - This game dosen't handle the case where the dealer has blackjack properly:
   - If the player doesn't also have blackjack, they are allowed to play a hand and attempt to match the dealer's 21
   - If the player does have blackjack, the player wins
