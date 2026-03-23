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
    private List<Card> deck;
    private Hand playerHand;
    private Hand computerHand;

    public Blackjack() {
        this.deck = new ArrayList<>();
        this.playerHand = new Hand(new ArrayList<Card>());
        this.computerHand = new Hand(new ArrayList<Card>());
    }

    public void startGame() {
        int bet, balance = 500;

        Scanner scanner = new Scanner(System.in);
        Validator validator = new Validator();

        // prompt for initial bet
        IO.println("You have $500");
        IO.print("How much would you like to bet? ");
        bet = scanner.nextInt();

        // validate bet with loop
        while (!validator.validateBet(bet, balance)) {
            IO.println("Invalid bet. Bet must be greater than 0 and less than your current balance.");
            IO.print("How much would you like to bet? ");
            bet = scanner.nextInt();
        }

        // setup game
        setupGame();

        // displays decks
        IO.println("Computer's hand: " + computerHand.getFirstCard().toString());
        IO.println("Your hand: " + playerHand.toString());
    }

    private void createDeck() {
        deck.clear(); // ensure deck is cleared

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    public void setupGame() {
        playerHand.resetHand();
        computerHand.resetHand();
        shuffleDeck();
        dealStartingHands();
    }

    public void shuffleDeck() {
        createDeck();
        Collections.shuffle(deck);
    }

    public void dealStartingHands() {
        playerHand.addCard(deck.removeFirst());
        computerHand.addCard(deck.removeFirst());
        playerHand.addCard(deck.removeFirst());
        computerHand.addCard(deck.removeFirst());
    }

    public Card dealCard() {
        return deck.removeFirst();
    }
}
