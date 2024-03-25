package co.setu.splitwise.dto.expense;

import lombok.Getter;

import java.util.List;

@Getter
public class AddExpenseDto {
    private Double amount;
    private String description;
    private List<String> splitBetween;
    private String createdBy;
    private String groupId;
}
