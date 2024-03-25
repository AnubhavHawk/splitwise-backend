package co.setu.splitwise.model;

import lombok.*;

@Getter
@Setter
@Builder
public class UserExpense {
    private String expenseId;
    private ExpenseStatus.Status status;
    private Double amount;
    private String groupId;
}
