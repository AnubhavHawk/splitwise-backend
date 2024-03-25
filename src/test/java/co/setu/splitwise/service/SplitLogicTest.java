package co.setu.splitwise.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SplitLogicTest {

    @Test
    public void testSplitLogicBetweenFourUsers() {
        double totalAmount = 100;
        int totalUsers = 4;
        double result = SplitService.calculateSplit(totalAmount, totalUsers);

        Assertions.assertEquals(25.0, result);
    }

    @Test
    public void testSplitLogicForSingleUsers() {
        double totalAmount = 100;
        int totalUsers = 1;
        double result = SplitService.calculateSplit(totalAmount, totalUsers);

        Assertions.assertEquals(100.0, result);
    }

    @Test
    public void testSplitLogicForZeroAmount() {
        double totalAmount = 0;
        int totalUsers = 4;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SplitService.calculateSplit(totalAmount, totalUsers);
        });

        String expectedMessage = "Amount should be greater than zero";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testSplitLogicForZeroUsers() {
        double totalAmount = 1000;
        int totalUsers = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SplitService.calculateSplit(totalAmount, totalUsers);
        });

        String expectedMessage = "Users should be more than zero";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.equals(expectedMessage));
    }
}
