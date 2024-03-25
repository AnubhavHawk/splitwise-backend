package co.setu.splitwise.controller;

import co.setu.splitwise.dto.expense.AddExpenseDto;
import co.setu.splitwise.dto.expense.DeleteExpenseDto;
import co.setu.splitwise.dto.expense.UpdateExpenseDto;
import co.setu.splitwise.model.Expense;
import co.setu.splitwise.service.ExpenseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static co.setu.splitwise.util.Util.failedJsonResponse;
import static co.setu.splitwise.util.Util.jsonResponse;

@Api(tags = "Expense API", consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ExpenseApiController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/expense/get-for-user/{userId}")
    public ResponseEntity getExpenseForUser(@PathVariable String userId) {
        try {
            return jsonResponse(
                    "userId", userId,
                    "expenses", expenseService.getExpenseForUser(userId));
        }
        catch (IllegalArgumentException ex) {
            return failedJsonResponse(ex.getMessage());
        }
    }

    @PostMapping("/expense/add")
    public ResponseEntity addExpense(@RequestBody AddExpenseDto addExpenseDto) {
        try {
            Expense created = expenseService.addExpense(addExpenseDto);
            return jsonResponse("expenseId", created.getExpenseId());
        }
        catch (IllegalArgumentException ex) {
            return failedJsonResponse(ex.getMessage().trim());
        }
    }

    @PutMapping("/expense/update")
    public ResponseEntity updateExpense(@RequestBody UpdateExpenseDto updateExpenseDto) {
        try {
            Expense updated = expenseService.updateExpense(updateExpenseDto);
            return jsonResponse(
                    "expenseId", updated.getExpenseId(),
                    "status", updated.getStatus(),
                    "totalAmount", updated.getTotalAmount());
        }
        catch (IllegalArgumentException ex) {
            return failedJsonResponse(ex.getMessage().trim());
        }
    }

    @DeleteMapping("/expense/delete")
    ResponseEntity deleteExpense(@RequestBody DeleteExpenseDto deleteExpenseDto) {
        try {
            expenseService.deleteExpense(deleteExpenseDto.getExpenseId());
            return jsonResponse("deleted", deleteExpenseDto.getExpenseId(), "deletedAt", LocalDateTime.now().toString());
        }
        catch (Exception e) {
            return failedJsonResponse(e.getMessage());
        }
    }
}