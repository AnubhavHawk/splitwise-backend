package co.setu.splitwise.service;

import org.springframework.stereotype.Service;

@Service
public class SplitService {

    public static double calculateSplit(Double totalAmount, int totalUsers) {
        if(totalAmount == 0) {
            throw new IllegalArgumentException("Amount should be greater than zero");
        }
        if(totalUsers == 0) {
            throw new IllegalArgumentException("Users should be more than zero");
        }
        return totalAmount/totalUsers;
    }
}
