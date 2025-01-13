package com.EffortlyTimeTracker.unit;

import com.EffortlyTimeTracker.repository.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Profile("postgres")
public class JpaUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    // Inject the repository based on the active database configuration
    public JpaUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
