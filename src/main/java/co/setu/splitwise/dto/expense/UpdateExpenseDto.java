package co.setu.splitwise.dto.expense;

import co.setu.splitwise.model.ExpenseStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateExpenseDto {
    private String expenseId;
    private List<IndividualExpense> expenseBreakdown;

    @Getter
    public static class IndividualExpense {
        private String userId;
        private ExpenseStatus.Status status;
    }
}
