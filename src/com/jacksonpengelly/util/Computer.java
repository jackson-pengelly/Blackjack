package com.jacksonpengelly.util;

import com.jacksonpengelly.main.Blackjack;
import com.jacksonpengelly.models.Hand;

public class Computer {
    public static void computerChoice(Blackjack game, Hand hand) {
        while (true) {
            int total = hand.getTotal();

            if (total < 17) hand.addCard(game.dealCard());
            else if (total == 17 && hand.isSoft()) hand.addCard(game.dealCard());
            else break;
        }
    }
}
