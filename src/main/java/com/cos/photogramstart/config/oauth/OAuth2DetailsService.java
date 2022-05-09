package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetalis;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepository;
	//private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oauth2User =  super.loadUser(userRequest);
		
		Map<String, Object> userInfo = oauth2User.getAttributes();    
		
		String username = "facebook_"+(String)userInfo.get("id");
		String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());	//DI 하지 않고 바로 객체 생성.
		String name = (String)userInfo.get("name");
		String email = (String)userInfo.get("email");
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {	// 페이스북 최초 로그인
			User user = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.name(name)
					.role("ROLE_USER")	// role 추가.
					.build();
			//return userRepository.save(user);
			return new PrincipalDetalis(userRepository.save(user), oauth2User.getAttributes());
		}
		else {	// 페이스북으로 이미 회원가입이 됨.
			return new PrincipalDetalis(userEntity,oauth2User.getAttributes());
		}
	}
}
