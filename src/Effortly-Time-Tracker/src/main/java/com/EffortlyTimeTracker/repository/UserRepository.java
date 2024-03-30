package com.EffortlyTimeTracker.repository;

import com.EffortlyTimeTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// CrudRepository
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
