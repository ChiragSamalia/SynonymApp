package com.Assignment.Synonym.Security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class CustomConfiguration implements OAuth2User {

	String email;
	private OAuth2User oauth2User;

	public CustomConfiguration(OAuth2User oauth2User,String email) {
		this.oauth2User = oauth2User;
		this.email=email;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		System.out.println("oauth2User " + oauth2User.getAttribute("login"));
		System.out.println("yes populated "+email);

		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		if (oauth2User.getAttribute("email").equals(email)) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		}
		return authorities;
	}

	@Override
	public String getName() {
		return oauth2User.getAttribute("name");
	}

}
