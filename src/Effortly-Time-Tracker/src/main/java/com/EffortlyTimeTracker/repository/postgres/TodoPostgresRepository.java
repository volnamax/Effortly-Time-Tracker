package com.EffortlyTimeTracker.repository.postgres;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.repository.ITodoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("postgres")
public interface TodoPostgresRepository extends JpaRepository<TodoNodeEntity, Integer>, ITodoRepository {
    // Modify the query to use the correct field name in the UserEntity
    @Query("SELECT t FROM TodoNodeEntity t WHERE t.user.userId = :userId")
    List<TodoNodeEntity> findByUserId(@Param("userId") Integer userId);
}
