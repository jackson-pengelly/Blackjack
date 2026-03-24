package com.jacksonpengelly.util;

public class Validator {
    public Validator() {}

    public boolean validateBet(int bet, int balance) {
        return bet > 0 && bet <= balance;
    }

    public boolean validateDouble(int bet, int balance) {
        return bet << 1 <= balance;
    }
}
