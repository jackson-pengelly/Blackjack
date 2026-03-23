package com.jacksonpengelly.util;

import com.jacksonpengelly.main.Blackjack;

public class Validator {
    public Validator() {}
    public boolean validateBet(int bet, int balance) {
        if (bet > balance) return false;
        if (bet <= 0) return false;

        return true;
    }
}
