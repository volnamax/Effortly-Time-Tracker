package com.EffortlyTimeTracker.repository.postgres;


import com.EffortlyTimeTracker.entity.InactiveTaskEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("postgres")
public interface InactiveTaskRepository extends JpaRepository<InactiveTaskEntity, Integer> {
}