package com.alejfneto.dscomerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscomerce.dto.UserDTO;
import com.alejfneto.dscomerce.entities.Role;
import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.projections.UserDetailsProjection;
import com.alejfneto.dscomerce.repositories.UserRepository;
import com.alejfneto.dscomerce.utils.CustomUserUtil;



@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private CustomUserUtil customUserUtil;

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
	
	protected User authenticated () {
		try {
			String username = customUserUtil.getLoggedUserName();
			User user = repository.findByEmail(username).get();
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException("Email not found!");
		}
	}
	
	@Transactional (readOnly = true)
	public UserDTO getMe() {
		User user = authenticated();
		UserDTO userDTO = new UserDTO(user);
		return userDTO;
	}

}
