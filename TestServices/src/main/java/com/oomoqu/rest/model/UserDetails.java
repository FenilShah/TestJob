package com.oomoqu.rest.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class UserDetails extends org.springframework.security.core.userdetails.User {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}
	
	User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

}
