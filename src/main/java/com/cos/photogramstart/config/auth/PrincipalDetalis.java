package com.cos.photogramstart.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class PrincipalDetalis implements UserDetails , OAuth2User{

	private static final long serialVersionUID = 1L;
	
	private User user;
	private Map<String, Object> attributes;

	public PrincipalDetalis(User user) {	// 일반 로그인시 사용할 생성자.
		this.user = user;
	}
	
	public PrincipalDetalis(User user, Map<String,Object> attributes) {	// 페이스북 로그인시 사용할 생성자.
		this.user = user;
		this.attributes = attributes;
	}
	
	@Override	//권한을 가져오는 함수
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한을 여러개 가지고 있는 계정이 있을 수 있기 때문에 Collection 타입이 리턴 타입이다.
		// 즉, 단순히 user.getRole();로 처리할 수 없다.
		/*
		// 1. 정석적인 방법 (복잡)
		Collection<GrantedAuthority> collector = new ArrayList<>();	//비어있는 Collection객체
		collector.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
				
		return collector;
		*/
		// 2. 람다식으로 간단하게 표현
		Collection<GrantedAuthority> collector = new ArrayList<>();
		collector.add( () -> { return user.getRole(); } );
		return collector;
		// why?
		// collector.add() 메서드의 파라미터는 GrantedAuthority 타입의 인터페이스이다.
		// 인터페이스를 파라미터로 하기 때문에 인터페이스의 메서드를 구현해야만 한다. (익명이너클래스 혹은 외부클래스로)
		// 이때, GrantedAuthority의 추상메서드는 getAuthority() 하나이다. 
		// 목적은 파라미터로 함수를 넘겨주고 싶은 것. 자바는 함수를 직접 파라미터로 넘길 수 없기 때문에
		// 인터페이스를 사옹한다. 이것을 람다식으로 표현함.
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override	// 계정만료 유무 
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override	// 계정잠금 유무 
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override	// 계정 권한 만료 유무 
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override	// 계정 활성화 유무 (false 이면 로그인 불가)
	public boolean isEnabled() {
		return true;
	}

	// OAuth2User의 구현메서드
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;	//{id = 232324,name=홍길동,email=hong@nate.com} 
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

}
