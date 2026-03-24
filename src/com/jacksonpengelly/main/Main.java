package com.jacksonpengelly.main;

import java.util.Scanner;

public class Main {
    void main() {
        Blackjack game = new Blackjack();
        Scanner scanner = new Scanner(System.in);

        // start game
        String answer;
        do {
            game.startGame();
            IO.print("Would you like to start a new game? (y/n) ");
            answer = scanner.nextLine();
        } while (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
    }
}
