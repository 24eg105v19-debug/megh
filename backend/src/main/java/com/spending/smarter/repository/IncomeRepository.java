package com.spending.smarter.repository;

import com.spending.smarter.model.Income;
import com.spending.smarter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUserAndIncomeDateBetween(User user, LocalDate start, LocalDate end);
    
    List<Income> findByUserAndIncomeDate(User user, LocalDate date);
    
    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user = :user AND i.incomeDate BETWEEN :start AND :end")
    BigDecimal getTotalIncomeByUserAndDateRange(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);
    
    List<Income> findByUserAndIsRecurringTrue(User user);
}