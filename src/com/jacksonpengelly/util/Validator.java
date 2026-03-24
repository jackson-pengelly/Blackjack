package com.jacksonpengelly.util;

public class Validator {
    public Validator() {}
    public boolean validateBet(int bet, int balance) {
        if (bet > balance) return false;
        return bet > 0;
    }
}
