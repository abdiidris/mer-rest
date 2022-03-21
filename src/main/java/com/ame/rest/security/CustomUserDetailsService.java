
package com.ame.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ame.rest.user.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private final UserRepository repository;

	@Autowired
	public CustomUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	public com.ame.rest.user.User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.repository.findByEmail(authentication.getName());
	}

	public UserDetails getCurrentUserDetails(){
		com.ame.rest.user.User current = getCurrentUser();
		if (current!=null)
			return loadUserByUsername(current.getEmail());
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		com.ame.rest.user.User user = this.repository.findByEmail(email);
		return new User(user.getEmail(), user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getRoles()));
	}

}