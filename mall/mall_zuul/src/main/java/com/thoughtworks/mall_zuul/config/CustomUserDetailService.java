package com.thoughtworks.mall_zuul.config;


import com.thoughtworks.mall_zuul.entity.JwtUser;
import com.thoughtworks.mall_zuul.entity.User;
import com.thoughtworks.mall_zuul.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException(String.format("%s doesn't exist!", username)));

        return new JwtUser(user);
    }
}
