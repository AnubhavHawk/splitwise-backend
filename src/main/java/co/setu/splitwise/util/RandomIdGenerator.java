package co.setu.splitwise.util;

import java.util.UUID;

public class RandomIdGenerator {
    public static String generateRandomId(int length) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }
    public static String generateRandomId() {
        return generateRandomId(15);
    }
}
