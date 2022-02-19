package com.postmission.security.auth;

import com.postmission.exceptions.NotExistException;
import com.postmission.model.User;
import com.postmission.model.enums.ErrorMessage;
import com.postmission.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        return new PrincipleDetails(user);
    }
}
