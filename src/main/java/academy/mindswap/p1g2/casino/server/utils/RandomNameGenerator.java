package academy.mindswap.p1g2.casino.server.utils;

import academy.mindswap.p1g2.casino.server.ClientHandler;

public class RandomNameGenerator {
    private static final String[] names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Henry", "Ivy", "Jack", "Kelly", "Leo", "Mia", "Noah", "Olivia", "Peter", "Quinn", "Rose", "Samuel", "Tara", "Uma", "Victor", "Wendy", "Xavier", "Yara", "Zane", "Adams", "Baker", "Clark", "Davis", "Edwards", "Fisher", "Gomez", "Hall", "Irwin", "Johnson", "Keller", "Lopez", "Morgan", "Nelson", "Owens", "Perez", "Quinn", "Reed", "Smith", "Taylor", "Underwood", "Valdez", "Walker", "Xiao", "Young", "Zimmerman"};

    private RandomNameGenerator() {
    }

    public static String generateRandomName() {
        return names[ClientHandler.getNumberOfClients() - 1];
    }
}
