package com.jacksonpengelly.main;

import com.jacksonpengelly.models.Card;
import com.jacksonpengelly.models.Hand;
import com.jacksonpengelly.models.Rank;
import com.jacksonpengelly.models.Suit;
import com.jacksonpengelly.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Blackjack {
    private int balance = 500;

    private List<Card> deck;
    private Hand playerHand;
    private Hand computerHand;

    public Blackjack() {
        this.deck = new ArrayList<>();
        this.playerHand = new Hand(new ArrayList<Card>());
        this.computerHand = new Hand(new ArrayList<Card>());
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
            if (playerHand.totalHand() == 21 && computerHand.totalHand() != 21) {
                updateBalance(bet, "blackjack");
                System.out.println("You got blackjack. You win! Your balance is now $" + balance + ".");
                roundOver = true;
            } else if (computerHand.totalHand() == 21 && playerHand.totalHand() != 21) {
                updateBalance(bet, "loss");
                System.out.println("Computer got blackjack. You lose. Your balance is now $" + balance + ".");
                roundOver = true;
            } else if (computerHand.totalHand() == 21 && playerHand.totalHand() == 21) {
                System.out.println("You both got blackjack. You tied. Your balance is now $" + balance + ".");
                roundOver = true;
            }

            // game loop
            while (!roundOver) {
                // displays decks
                IO.println("Computer's upcard: " + computerHand.getUpcard().toString());
                IO.println("Your hand: " + playerHand.toString());

                // prompt user for hit or stand
                IO.print("Hit or stand? ");
                answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("hit")) {
                    playerHand.addCard(dealCard());
                    if (playerHand.totalHand() > 21) {
                        roundOver = true;
                    }
                } else {
                    break;
                }
            }

            if (playerHand.totalHand() > 21) {
                updateBalance(bet, "loss");
                IO.println("Busted! Your total: " + playerHand.totalHand() + ". Your balance is now $" + balance + ".");
            } else if (playerHand.totalHand() > computerHand.totalHand() || computerHand.totalHand() > 21) {
                updateBalance(bet, "win");
                IO.println("You win! Your balance is now $" + balance + ".");
            } else if (playerHand.totalHand() < computerHand.totalHand()) {
                updateBalance(bet, "loss");
                IO.println("You lose. Your balance is now $" + balance + ".");
            } else {
                IO.println("It's a tie.");
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

    private Card dealCard() {
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