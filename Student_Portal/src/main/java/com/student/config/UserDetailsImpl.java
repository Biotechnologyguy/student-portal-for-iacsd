package com.student.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.student.entites.User;
import com.student.repository.UserRepository;

@Service
public class UserDetailsImpl implements UserDetailsService {

	@Autowired
	private UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User s = repo.findByEmail(username);
		if (s == null) {
			throw new UsernameNotFoundException("User Not Exist");
		} else {

			return new CustomUserDetails(s);
		}

	}

}
