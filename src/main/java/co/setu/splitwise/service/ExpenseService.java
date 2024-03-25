package co.setu.splitwise.service;

import co.setu.splitwise.dto.expense.AddExpenseDto;
import co.setu.splitwise.dto.expense.UpdateExpenseDto;
import co.setu.splitwise.model.*;
import co.setu.splitwise.repository.ExpenseRepository;
import co.setu.splitwise.repository.GroupRepository;
import co.setu.splitwise.repository.SplitBetweenRepository;
import co.setu.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static co.setu.splitwise.util.RandomIdGenerator.generateRandomId;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private SplitBetweenRepository splitBetweenRepository;

    @Autowired
    private UserRepository userRepository;

    public Expense addExpense(AddExpenseDto addExpenseDto) {
        // Step 1: Validate the values
        // Step 2: Prepare the expense
        Expense preparedExpense = validateAndPrepareExpenseObject(addExpenseDto);
        if(preparedExpense != null) {
            return expenseRepository.save(preparedExpense);
        }
        // Step 3: Persist to DB
        return null;
    }

    public Expense updateExpense(UpdateExpenseDto updateExpenseDto) {
        Expense existing = expenseRepository.findById(updateExpenseDto.getExpenseId()).orElse(null);
        if(existing == null) {
            throw new IllegalArgumentException("expenseId: " + updateExpenseDto.getExpenseId() + " does not exist");
        }

        // Validate if users are registered
        for(UpdateExpenseDto.IndividualExpense individualExpense: updateExpenseDto.getExpenseBreakdown()) {
            RegisteredUser user = userRepository.findById(individualExpense.getUserId()).orElse(null);
            if(user == null) {
                throw new IllegalArgumentException("user: " + individualExpense.getUserId() + " does not exist");
            }
        }
        List<SplitBetween> usersToPay = existing.getSplitBetween(); // Update for the provided users

        List<SplitBetween> listOfUsersPaying = new ArrayList<>();
        for(SplitBetween userFromDb: usersToPay) {
            for(UpdateExpenseDto.IndividualExpense individualExpense: updateExpenseDto.getExpenseBreakdown()) {
                if(userFromDb.getToBePaidBy() == null) {
                    throw new IllegalArgumentException("User is not valid");
                }
                if(userFromDb.getToBePaidBy().getUserId().equals(individualExpense.getUserId())) {
                    userFromDb.setStatus(individualExpense.getStatus());
                    listOfUsersPaying.add(userFromDb);
//                    splitBetweenRepository.save(userFromDb); // Don't call the DB everytime
                }
            }
        }
        splitBetweenRepository.saveAll(listOfUsersPaying);

        // If all have paid, then mark the status as CLEARED for the expense
        long clearedExpensePerHead = existing.getSplitBetween().stream()
                .filter(userWhoPaied -> userWhoPaied.getStatus() == ExpenseStatus.Status.PAID)
                .count();

        if(clearedExpensePerHead == existing.getSplitBetween().size()) {
            // All are cleared
            existing.setStatus(ExpenseStatus.CLEARED);
        }
        else {
            existing.setStatus(ExpenseStatus.PENDING);
        }
        expenseRepository.save(existing);

        return existing;
    }

    public List<UserExpense> getExpenseForUser(String userId) {
        List<Object[]> expenseOnUser = expenseRepository.getExpenseByUserClass(userId);
        List<UserExpense> userExpenseList = null;
        if(expenseOnUser != null && expenseOnUser.size() > 0) {
            userExpenseList = new ArrayList<>();
            for(Object[] o: expenseOnUser) {
                userExpenseList.add(mapUserExpense(o));
            }
        }
        else {
            throw new IllegalArgumentException("User " + userId + " does not exist");
        }

        return userExpenseList;
    }

    public Boolean deleteExpense(String expenseId) {
        try {
            expenseRepository.deleteById(expenseId);
            return true;
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("expenseId " + expenseId + " does not exist");
        }
    }

    private UserExpense mapUserExpense(Object[] o) {
        return UserExpense.builder()
                .expenseId((String) o[0])
                .status(ExpenseStatus.Status.valueOf((String) o[1]))
                .groupId((String) o[2])
                .amount((Double) o[3])
                .build();
    }

    private Expense validateAndPrepareExpenseObject(AddExpenseDto expenseDto) {
        Expense expenseObject = null;
        String validationMessage = "";
        Expense.ExpenseBuilder expenseBuilder = Expense.builder();
        if(expenseDto == null || expenseDto.getCreatedBy() == null) {
            validationMessage = "\nExpense Object can not be null";
        }
        RegisteredUser createdBy = userRepository.getById(expenseDto.getCreatedBy());
        if(createdBy == null) {
            validationMessage += "\ncreatedBy user is not registered";
        }
        if(expenseDto.getSplitBetween() == null || expenseDto.getSplitBetween().size() == 0) {
            validationMessage += "\nsplitBetween can not be empty";
        }
        if(expenseDto.getGroupId() != null) {
            Group group = groupRepository.findById(expenseDto.getGroupId()).orElse(null);
            if(group == null) {
                validationMessage += "\nGroup: " + expenseDto.getGroupId() + " doesn't exist";
            }
        }
        if(!validationMessage.equals("")) {
            throw new IllegalArgumentException(validationMessage);
        }
        else { // All Validations are complete
            List<RegisteredUser> registeredUserListToParticipateInSplitting = new ArrayList<>();
            for(String splitBetweenUserId: expenseDto.getSplitBetween()) {
                RegisteredUser user = userRepository.findById(splitBetweenUserId).orElse(null);
                if(user == null) {
                    validationMessage += "\nuser: " + splitBetweenUserId + " is not registered";
                }
                registeredUserListToParticipateInSplitting.add(user);
            }
            if(registeredUserListToParticipateInSplitting.size() == expenseDto.getSplitBetween().size()) { // Only if all the users are registered on splitwise
                String groupId = expenseDto.getGroupId();

                expenseBuilder.expenseId(generateRandomId());
                expenseBuilder.description(expenseDto.getDescription());
                expenseBuilder.totalAmount(expenseDto.getAmount());
                expenseBuilder.createdBy(createdBy);
                expenseBuilder.status(ExpenseStatus.PENDING);
                addUsersToExpense(expenseBuilder, registeredUserListToParticipateInSplitting, groupId);


                expenseObject = expenseBuilder.build(); // Population complete
            }
            else {
                throw new IllegalArgumentException(validationMessage);
            }
        }

        return expenseObject;
    }

    private void addUsersToExpense(Expense.ExpenseBuilder expenseBuilder, List<RegisteredUser> splitBetweenUserList, String groupId) {
        List<SplitBetween> preparedSplit = new ArrayList<>();
        for(RegisteredUser user: splitBetweenUserList) {
            preparedSplit.add(getSplit(user, expenseBuilder.build(), splitBetweenUserList.size(), groupId));
        }
        expenseBuilder.splitBetween(preparedSplit);
    }

    private SplitBetween getSplit(RegisteredUser user, Expense expense, int userCount, String groupId) {
        SplitBetween.SplitBetweenBuilder splitBetween = SplitBetween.builder();
        splitBetween.splitId(generateRandomId())
                .expense(expense)
                .amountPerUser(SplitService.calculateSplit(expense.getTotalAmount(), userCount))
                .toBePaidBy(user)
                .groupId(groupId)
                .status(ExpenseStatus.Status.UNPAID)
                .build();
        return splitBetween.build();
    }
}