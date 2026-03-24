package com.jacksonpengelly.models;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Hand {
    private final ArrayList<Card> hand;

    public Hand(ArrayList<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }

    public int getTotal() {
        int total = 0;
        int aceCounter = 0;

        for (Card card : hand) {
            total += card.getValue();
            if (card.getValue() == 11) aceCounter++;
        }

        while (total > 21 && aceCounter > 0) {
            total -= 10;
            aceCounter--;
        }
        return total;
    }

    public String toString() {
        return hand.stream().map(Card::toString).collect(Collectors.joining(", "));
    }

    public Card getUpcard() {
        return hand.getFirst();
    }

    public void resetHand() {
        hand.clear();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public boolean isSoft() {
        int total = 0;
        int aceCounter = 0;

        for (Card card : hand) {
            total += card.getValue();
            if (card.getValue() == 11) aceCounter++;

            while (total > 21 && aceCounter > 0) {
                total -= 10;
                aceCounter--;
            }
        }
        return aceCounter > 0;
    }
}
