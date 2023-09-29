package com.alejfneto.dscomerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alejfneto.dscomerce.entities.Role;
import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.projections.UserDetailsProjection;
import com.alejfneto.dscomerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List <UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
			if (result.size() == 0) {
				throw new UsernameNotFoundException("Username not found!");
			}
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		return user;
	}

}
