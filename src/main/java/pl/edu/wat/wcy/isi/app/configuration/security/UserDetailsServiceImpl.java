package pl.edu.wat.wcy.isi.app.configuration.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLoginOrEmail(login, login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email : " + login));
        return UserPrinciple.build(user);
    }

}
