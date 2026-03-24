package com.jacksonpengelly.main;

import com.jacksonpengelly.models.Card;
import com.jacksonpengelly.models.Hand;
import com.jacksonpengelly.models.Rank;
import com.jacksonpengelly.models.Suit;
import com.jacksonpengelly.util.Computer;
import com.jacksonpengelly.util.Util;
import com.jacksonpengelly.util.Validator;

import java.io.IOException;
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
        Scanner scanner = new Scanner(System.in);
        Validator validator = new Validator();

        // load balance
        balance = Util.loadBalance();
        if (balance <= 0) balance = 500;

        // do while loop based on if user wants to play again or check out
        String answer;
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
            int turnCounter = 0;
            boolean roundOver = false;

            // check for black jack
            if (playerHand.getTotal() == 21 && computerHand.getTotal() != 21) {
                updateBalance(bet, "blackjack");
                IO.println("You got blackjack. You win! Your balance is now $" + balance + ".");

                IO.print("Would you like to play again? (y/n) ");
                answer = scanner.nextLine();
                continue;
            } else if (computerHand.getTotal() == 21 && playerHand.getTotal() != 21) {
                updateBalance(bet, "loss");
                IO.println("Computer got blackjack. You lose. Your balance is now $" + balance + ".");

                IO.print("Would you like to play again? (y/n) ");
                answer = scanner.nextLine();
                continue;
            } else if (computerHand.getTotal() == 21 && playerHand.getTotal() == 21) {
                IO.println("You both got blackjack. You tied. Your balance is $" + balance + ".");

                IO.print("Would you like to play again? (y/n) ");
                answer = scanner.nextLine();
                continue;
            }

            // game loop
            while (!roundOver) {
                // displays decks
                IO.println("Computer's upcard: " + computerHand.getUpcard().toString());
                IO.println("Your hand: " + playerHand);

                // prompt user for hit or stand
                if (turnCounter == 0) IO.print("Hit, double, or stand? ");
                else  IO.print("Hit or stand? ");
                answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("hit")) {
                    playerHand.addCard(dealCard());
                    if (checkBust(playerHand)) {
                        IO.println("Your hand: " + playerHand);
                        roundOver = true;
                    }
                } else if (answer.equalsIgnoreCase("double")) {
                    if (!validator.validateDouble(bet, balance)) {
                        IO.println("Doubling can not exceed balance. ");
                        continue;
                    }
                    playerHand.addCard(dealCard());
                    bet *= 2;

                    IO.println("Your hand: " + playerHand);
                    break;
                } else {
                    IO.println("Your hand: " + playerHand);
                    break;
                }
                turnCounter++;
            }
            if (playerHand.getTotal() <= 21) {
                Computer.computerChoice(this, computerHand);
            }
            IO.println("Computer's hand: " + computerHand);

            // general win checks
            if (checkBust(playerHand)) {
                updateBalance(bet, "loss");
                IO.println("Busted! Your total: " + playerHand.getTotal() + ". Your balance is now $" + balance + ".");
            } else if (checkBust(computerHand)) {
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

        try {
            Util.saveBalance(balance);
        } catch (IOException e) {
            throw new RuntimeException("Error saving balance.", e);
        }
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

    private boolean checkBust(Hand hand) { return hand.getTotal() > 21; }
}