package com.thoughtworks.securitydemo.config;

import com.thoughtworks.securitydemo.model.User;
import com.thoughtworks.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TwMallUserDetailService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(null);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("%s dosen't exist!", username));
		} else {
			return new com.thoughtworks.securitydemo.login.JwtUser(user);
		}
	}
}
