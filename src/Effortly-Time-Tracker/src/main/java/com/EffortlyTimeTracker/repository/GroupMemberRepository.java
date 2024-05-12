package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMermberEntity, Integer> {
}
