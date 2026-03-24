package com.jacksonpengelly.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {
    private final static String localAppData = System.getenv("LOCALAPPDATA");
    private final static Path folderPath = Paths.get(localAppData, "Blackjack", "data");
    private final static Path filePath = folderPath.resolve("balance.txt");

    public static void saveBalance(int balance) throws IOException {
        Files.createDirectories(folderPath);

        try (BufferedWriter br = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            br.write(Integer.toString(balance));
        }
    }

    public static int loadBalance() {
        if (!Files.exists(filePath)) return 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line = br.readLine();
            return (line != null) ? Integer.parseInt(line) : 0;
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Failed to load balance.", e);
        }
    }
}
