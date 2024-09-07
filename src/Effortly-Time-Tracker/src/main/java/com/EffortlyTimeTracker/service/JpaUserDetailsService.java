package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//todo ebat cho eto
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;


    public JpaUserDetailsService(
            @Value("${app.active-db}") String activeDb,
            @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
            @Qualifier("userMongoRepository") IUserRepository userMongoRepository
    ) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            this.userRepository = userPostgresRepository;
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            this.userRepository = userMongoRepository;
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
