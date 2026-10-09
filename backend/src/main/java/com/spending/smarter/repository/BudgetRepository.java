package com.spending.smarter.repository;

import com.spending.smarter.model.Budget;
import com.spending.smarter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqual(User user, LocalDate start, LocalDate end);
    
    Optional<Budget> findByUserAndCategoryAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            User user, com.spending.smarter.model.Category category, LocalDate start, LocalDate end);
    
    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.startDate <= :date AND b.endDate >= :date")
    List<Budget> findActiveBudgetsByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    List<Budget> findByUser(User user);
}