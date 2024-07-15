package com.alejfneto.dscomerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alejfneto.dscomerce.entities.User;
import com.alejfneto.dscomerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {
	
	@Autowired
	private UserService userService;
	
	public void validateSelfOrAdmin (Long userId) {
		User me = userService.authenticated();
//		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
//			throw new ForbiddenException ("Acesso negado!");
//		}
		if (me.hasRole("ROLE_ADMIN")){
			return;
		}
		if (!me.getId().equals(userId)) {
			throw new ForbiddenException ("Acesso negado! Você só pode acessar seus próprios pedidos.");
		}
				
	}

}
