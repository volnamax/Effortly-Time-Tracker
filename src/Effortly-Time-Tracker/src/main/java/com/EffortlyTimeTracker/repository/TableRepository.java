package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<TableEntity, Integer> {
}
