package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.entity.UserMongoEntity;
import com.EffortlyTimeTracker.mapper.UserMongoMapper;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.mongo.IMongoUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final IUserRepository userPostgresRepository;
    private final IMongoUserRepository userMongoRepository;
    private final String activeDb;

    public JpaUserDetailsService(
            @Value("${app.active-db}") String activeDb,
            @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
            @Qualifier("userMongoRepository") IMongoUserRepository userMongoRepository
    ) {
        this.activeDb = activeDb;
        this.userPostgresRepository = userPostgresRepository;
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.findByEmail(username)
                    .map(user -> new SecurityUser(user))
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findByEmail(username)
                    .map(user -> new SecurityUser(UserMongoMapper.toUserEntity(user)))
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }
}
