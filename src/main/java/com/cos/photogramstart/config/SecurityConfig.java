package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity	  // 해당 파일로 시큐리티를 활성
@Configuration	// IoC 등
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final OAuth2DetailsService oauth2DetailsService;
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super.configure(http);  // 기존의 super 를 제거 -> 기존 시큐리티가 가지고 있는 모든 기능 비활성화 
		http.csrf().disable();     // csrf 토큰 비활성
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/auth/signin")		// get 방식 
			.loginProcessingUrl("/auth/signin")   //post 방식 -> 스프링시큐리티가 로그인 프로세스 진행.
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()	//	form로그인도 하는데, oauth2로그인도 할거야!
			.userInfoEndpoint()	//	oauth2 로그인을 하면 최종 응답을 회원정보로 받을 수 있다.
			.userService(oauth2DetailsService);
	}
	
}
