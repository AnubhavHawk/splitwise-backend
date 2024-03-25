package co.setu.splitwise.repository;

import co.setu.splitwise.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    @Query(value = "SELECT e.expense_id, sb.status isPaid, sb.group_id, e.total_amount FROM expense e INNER JOIN expense_split_between es ON e.expense_id = es.expense_expense_id INNER JOIN split_between sb ON sb.split_id = es.split_between_split_id WHERE sb.to_be_paid_by_user_id = :userId", nativeQuery = true)
    List<Object[]> getExpenseByUserClass(@Param("userId")String userId);
}
