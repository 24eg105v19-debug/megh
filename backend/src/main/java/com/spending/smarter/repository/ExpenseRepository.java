package com.spending.smarter.repository;

import com.spending.smarter.model.Expense;
import com.spending.smarter.model.User;
import com.spending.smarter.dto.CategoryExpenseSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserAndExpenseDateBetween(User user, LocalDate start, LocalDate end);
    
    List<Expense> findByUserAndExpenseDate(User user, LocalDate date);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.expenseDate BETWEEN :start AND :end")
    BigDecimal getTotalExpensesByUserAndDateRange(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT new com.spending.smarter.dto.CategoryExpenseSummary(c.name, c.iconName, c.color, SUM(e.amount)) " +
           "FROM Expense e JOIN e.category c WHERE e.user = :user AND e.expenseDate BETWEEN :start AND :end " +
           "GROUP BY c.id, c.name, c.iconName, c.color ORDER BY SUM(e.amount) DESC")
    List<CategoryExpenseSummary> getExpensesByCategory(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
    List<Expense> findByUserAndIsRecurringTrue(User user);
}