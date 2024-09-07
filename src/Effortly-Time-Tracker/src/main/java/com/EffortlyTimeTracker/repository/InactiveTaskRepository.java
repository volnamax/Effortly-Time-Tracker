package com.EffortlyTimeTracker.repository;


import com.EffortlyTimeTracker.entity.InactiveTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InactiveTaskRepository extends JpaRepository<InactiveTaskEntity, Integer> {
}
