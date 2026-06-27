package com.kunwar.expense_manager.repository;

import com.kunwar.expense_manager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("Select c from Category c where c.userId IS NULL OR c.userId = :userId")
    List<Category> findAllByGlobalOrUserId(@Param("userId") String userId);
}
