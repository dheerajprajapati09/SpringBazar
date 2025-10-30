package in.dk.SpringBazaar.Security;

import in.dk.SpringBazaar.Entity.User;
import in.dk.SpringBazaar.Repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail= userRepository.findByEmail(email);
        if(byEmail.isEmpty())
        {
            throw new UsernameNotFoundException("User not found "+ email);
        }
        return byEmail.get();
    }

}