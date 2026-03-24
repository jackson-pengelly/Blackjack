package com.jacksonpengelly.main;

import com.jacksonpengelly.models.Card;
import com.jacksonpengelly.models.Hand;
import com.jacksonpengelly.models.Rank;
import com.jacksonpengelly.models.Suit;
import com.jacksonpengelly.util.Computer;
import com.jacksonpengelly.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Blackjack {
    private int balance = 500;

    private final List<Card> deck;
    private final Hand playerHand;
    private final Hand computerHand;

    public Blackjack() {
        this.deck = new ArrayList<>();
        this.playerHand = new Hand(new ArrayList<>());
        this.computerHand = new Hand(new ArrayList<>());
    }

    public void startGame() {
        String answer = "";
        Scanner scanner = new Scanner(System.in);
        Validator validator = new Validator();

        // do while loop based on if user wants to play again or check out
        do {
            // prompt for initial bet
            IO.println("You have $" + balance + ".");
            IO.print("How much would you like to bet? ");
            int bet = scanner.nextInt();
            scanner.nextLine();

            // validate bet with loop
            while (!validator.validateBet(bet, balance)) {
                IO.println("Invalid bet. Bet must be greater than 0 and less than your current balance.");
                IO.print("How much would you like to bet? ");
                bet = scanner.nextInt();
                scanner.nextLine();
            }

            // setup game
            setupGame();
            boolean roundOver = false;

            // check for black jack
            if (playerHand.getTotal() == 21 && computerHand.getTotal() != 21) {
                updateBalance(bet, "blackjack");
                System.out.println("You got blackjack. You win! Your balance is now $" + balance + ".");
                continue;
            } else if (computerHand.getTotal() == 21 && playerHand.getTotal() != 21) {
                updateBalance(bet, "loss");
                System.out.println("Computer got blackjack. You lose. Your balance is now $" + balance + ".");
                continue;
            } else if (computerHand.getTotal() == 21 && playerHand.getTotal() == 21) {
                System.out.println("You both got blackjack. You tied. Your balance is now $" + balance + ".");
                continue;
            }

            // game loop
            while (!roundOver) {
                // displays decks
                IO.println("Computer's upcard: " + computerHand.getUpcard().toString());
                IO.println("Your hand: " + playerHand);

                // prompt user for hit or stand
                IO.print("Hit or stand? ");
                answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("hit")) {
                    playerHand.addCard(dealCard());
                    if (playerHand.getTotal() > 21) {
                        roundOver = true;
                    }
                } else {
                    break;
                }
            }
            if (playerHand.getTotal() <= 21) {
                Computer.computerChoice(this, computerHand);
            }
            IO.println("Computer's hand: " + computerHand);

            // general win checks
            if (playerHand.getTotal() > 21) {
                updateBalance(bet, "loss");
                IO.println("Busted! Your total: " + playerHand.getTotal() + ". Your balance is now $" + balance + ".");
            } else if (computerHand.getTotal() > 21) {
                updateBalance(bet, "win");
                IO.println("The computer busted. You Win! Your balance is now $" + balance + ".");
            } else if (playerHand.getTotal() > computerHand.getTotal()) {
                updateBalance(bet, "win");
                IO.println("You win! Your balance is now $" + balance + ".");
            } else if (playerHand.getTotal() < computerHand.getTotal()) {
                updateBalance(bet, "loss");
                IO.println("You lose. Your balance is $" + balance + ".");
            } else {
                IO.println("It's a tie. Your balance is $" + balance + ".");
            }

            // check if balance is less than 0 to end game
            if (balance <= 0) {
                IO.println("You're out of money. Game over.");
                break;
            }

            // ask if user would like to play again
            IO.print("Would you like to play again? (y/n) ");
            answer = scanner.nextLine();
        } while (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
        IO.println("Thank you for playing! Your final balance was $" + balance + ".");
    }

    private void createDeck() {
        deck.clear(); // ensure deck is cleared

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    private void setupGame() {
        playerHand.resetHand();
        computerHand.resetHand();
        shuffleDeck();
        dealStartingHands();
    }

    private void shuffleDeck() {
        createDeck();
        Collections.shuffle(deck);
    }

    private void dealStartingHands() {
        playerHand.addCard(deck.removeFirst());
        computerHand.addCard(deck.removeFirst());
        playerHand.addCard(deck.removeFirst());
        computerHand.addCard(deck.removeFirst());
    }

    public Card dealCard() {
        if (deck.isEmpty()) shuffleDeck();
        return deck.removeFirst();
    }

    private void updateBalance(int bet, String state) {
        switch (state) {
            case "loss":
                balance -= bet;
                break;
            case "win":
                balance += bet;
                break;
            case "blackjack":
                balance += (int) (bet * 1.5);
                break;
        }
    }
}