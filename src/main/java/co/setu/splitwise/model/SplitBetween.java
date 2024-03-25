package co.setu.splitwise.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SplitBetween {
    @Id
    private String splitId;
    @ManyToOne
    private Expense expense;

    @OneToOne
    private RegisteredUser toBePaidBy;
    private Double amountPerUser;
    private String groupId; // If it is from a group then it will not be null
    @Enumerated(EnumType.STRING)
    private ExpenseStatus.Status status;
}
