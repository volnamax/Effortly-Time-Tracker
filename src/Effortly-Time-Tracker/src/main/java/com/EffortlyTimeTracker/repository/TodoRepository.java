package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoNodeEntity, Integer> {
    @Query("SELECT t FROM TodoNodeEntity t WHERE t.user.userId = :userId")
    List<TodoNodeEntity> findByUserId(@Param("userId") Integer userId);

}
