package com.spending.smarter.repository;

import com.spending.smarter.model.Tip;
import com.spending.smarter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {
    List<Tip> findByUserOrderByCreatedAtDesc(User user);

    @Modifying
    @Query("DELETE FROM Tip t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);
}