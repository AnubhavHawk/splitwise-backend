package co.setu.splitwise.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    private String expenseId;
    private String description;
    private Double totalAmount;
    @OneToOne
    private RegisteredUser createdBy;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private ExpenseStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SplitBetween> splitBetween;
}